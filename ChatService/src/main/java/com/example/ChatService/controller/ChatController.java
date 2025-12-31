package com.example.ChatService.controller;

import com.example.ChatService.DTO.MessageDTO;
import com.example.ChatService.DTO.MessageRequestDTO;
import com.example.ChatService.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * STOMP WebSocket Controller
 * Handles real-time message sending
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    /**
     * Handle incoming messages via STOMP
     * /app/message.send
     */
    @MessageMapping("/message.send")
    public void sendMessage(@Payload MessageRequestDTO request, 
                           SimpMessageHeaderAccessor headerAccessor,
                           Principal principal) {
        try {
            // Get userId from principal (set by AuthChannelInterceptor)
            Long senderId = Long.parseLong(principal.getName());
            request.setSenderId(senderId);
            
            log.info("Received message from userId: {} to recipientId: {}", senderId, request.getRecipientId());
            
            // Process message (sends to Kafka for persistence)
            chatService.processMessage(request);
            
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send message", e);
        }
    }
}

