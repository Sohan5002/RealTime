package  com.example.GatewayService.auth;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor   // ✅ auto inject JwtUtil
@Slf4j                     // ✅ logger available as 'log'
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        // ✅ check if request is Servlet type
        if (request instanceof ServletServerHttpRequest servletReq) {
            HttpServletRequest httpReq = servletReq.getServletRequest();
            String token = extractToken(httpReq);

            if (token == null) {
                log.warn("Handshake failed: Token missing");
                return false;
            }

            try {
                String userId = jwtUtil.getUserId(token);
                attributes.put("userId", userId);
                attributes.put("token", token);
                log.info("✅ Handshake successful for userId: {}", userId);
                return true;
            } catch (Exception ex) {
                log.error("❌ Invalid token during handshake: {}", ex.getMessage());
                return false;
            }
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        log.debug("Handshake completed.");
    }

    // 🔹 Token extract helper method
    private String extractToken(HttpServletRequest httpReq) {
        String token = httpReq.getParameter("token");
        if (token == null) {
            String auth = httpReq.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                token = auth.substring(7);
            }
        }
        return token;
    }
}
