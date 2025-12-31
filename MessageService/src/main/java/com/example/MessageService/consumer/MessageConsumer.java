package com.example.MessageService.consumer;

import com.example.MessageService.dto.MessageDTO;
import com.example.MessageService.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka Consumer for Chat Messages
 * Listens to chat.messages topic and persists messages
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final MessageService messageService;

    /**
     * Consume messages from Kafka topic: chat.messages
     */
    @KafkaListener(topics = "chat.messages", groupId = "message-service")
    public void consume(MessageDTO messageDTO) {
        try {
            log.info("Received message from Kafka: senderId={}, recipientId={}, content={}", 
                    messageDTO.getSenderId(), messageDTO.getRecipientId(), 
                    messageDTO.getContent() != null ? messageDTO.getContent().substring(0, Math.min(50, messageDTO.getContent().length())) : "");
            
            // Save message to database
            messageService.saveMessage(messageDTO);
            
            log.info("Message processed successfully: senderId={}, recipientId={}", 
                    messageDTO.getSenderId(), messageDTO.getRecipientId());
        } catch (Exception e) {
            log.error("Error consuming message: {}", e.getMessage(), e);
            // In production, implement retry mechanism or dead letter queue
        }
    }
}

