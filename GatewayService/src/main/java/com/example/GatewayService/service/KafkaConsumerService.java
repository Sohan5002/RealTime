package com.example.GatewayService.service;
//import com.example.wsgateway.handler.ChatWebSocketHandler;
import com.example.GatewayService.handler.ChatWebSocketHandler;
import com.example.GatewayService.service.RedisConnectionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final RedisConnectionManager connectionManager;
    private final ChatWebSocketHandler wsHandler;
    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "chat.messages.delivery", groupId = "ws-gateway-group")
    public void onMessage(String raw) {
        try {
            var node = mapper.readTree(raw);
            String recipientId = node.get("recipientId").asText();
            String payload = node.get("payload").toString();

            var connections = connectionManager.getConnections(recipientId);
            if (connections != null && !connections.isEmpty()) {
                for (String connId : connections) {
                    wsHandler.sendToSessionId(connId, payload);
                }
            } else {
                log.info("User {} offline, message skipped.", recipientId);
            }
        } catch (Exception ex) {
            log.error("Error processing message: {}", raw, ex);
        }
    }
}
