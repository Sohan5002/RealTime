package com.example.GatewayService.handler;

import com.example.GatewayService.dto.ChatMessage;
import com.example.GatewayService.service.KafkaProducerService;
import com.example.GatewayService.service.RedisConnectionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final RedisConnectionManager connectionManager;
    private final KafkaProducerService kafkaProducer;

    // local session store (per instance)
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(RedisConnectionManager connectionManager,
                                KafkaProducerService kafkaProducer) {
        this.connectionManager = connectionManager;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        if (userId == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("No userId"));
            return;
        }
        sessions.put(session.getId(), session);
        connectionManager.addConnection(userId, session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessage msg = mapper.readValue(payload, ChatMessage.class);

        // simple validation
        if ("message.send".equals(msg.getType())) {
            // attach senderId from handshake for security
            String senderId = (String) session.getAttributes().get("userId");
            msg.getPayload().put("senderId", senderId);

            // publish to Kafka
            kafkaProducer.publish("chat.messages", mapper.writeValueAsString(msg));
            // optimistic response to sender
            session.sendMessage(new TextMessage("{\"type\":\"message.queued\",\"tempId\":\"" + msg.getTempId() + "\"}"));
        } else {
            // handle other types (typing, presence) if needed
            session.sendMessage(new TextMessage("{\"type\":\"unsupported\"}"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        String userId = (String) session.getAttributes().get("userId");
        if (userId != null) {
            connectionManager.removeConnection(userId, session.getId());
        }
    }

    // method used by KafkaConsumerService to send message to a particular connection id
    public boolean sendToSessionId(String connectionId, String text) {
        WebSocketSession s = sessions.get(connectionId);
        if (s != null && s.isOpen()) {
            try {
                s.sendMessage(new TextMessage(text));
                return true;
            } catch (Exception e) {
                // log
            }
        }
        return false;
    }
}
