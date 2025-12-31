package com.example.ChatService.config.interceptor;

import com.example.ChatService.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

/**
 * Authentication Interceptor for STOMP WebSocket
 * Validates JWT token from STOMP headers
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Extract token from headers
            List<String> tokenList = accessor.getNativeHeader("Authorization");
            String token = null;
            
            if (tokenList != null && !tokenList.isEmpty()) {
                String authHeader = tokenList.get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                } else {
                    token = authHeader; // Try as direct token
                }
            }
            
            // Also try query parameter
            if (token == null || token.isEmpty()) {
                token = accessor.getFirstNativeHeader("token");
            }
            
            if (token != null && !token.isEmpty()) {
                try {
                    // Validate token and extract userId
                    Long userId = jwtUtil.getUserId(token);
                    String username = jwtUtil.getUsername(token);
                    
                    // Create authentication object
                    Principal principal = () -> userId.toString();
                    Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, null);
                    accessor.setUser(auth);
                    
                    // Store userId in session attributes
                    java.util.Map<String, Object> sessionAttrs = accessor.getSessionAttributes();
                    if (sessionAttrs != null) {
                        sessionAttrs.put("userId", userId);
                        sessionAttrs.put("username", username);
                    }
                    
                    log.info("WebSocket connection authenticated for userId: {}", userId);
                } catch (Exception e) {
                    log.error("WebSocket authentication failed: {}", e.getMessage());
                    throw new RuntimeException("Authentication failed", e);
                }
            } else {
                log.warn("No token provided in WebSocket connection");
                throw new RuntimeException("Authentication token required");
            }
        }
        
        return message;
    }
}

