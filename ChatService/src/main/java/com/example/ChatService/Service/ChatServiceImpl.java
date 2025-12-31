package com.example.ChatService.service;

import com.example.ChatService.DTO.MessageDTO;
import com.example.ChatService.DTO.MessageRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Chat Service Implementation
 * Sends messages to Kafka for processing by MessageService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final String MESSAGE_TOPIC = "chat.messages";

    @Override
    public MessageDTO processMessage(MessageRequestDTO request) {
        try {
            // Create message DTO
            MessageDTO messageDTO = MessageDTO.builder()
                    .senderId(request.getSenderId())
                    .recipientId(request.getRecipientId())
                    .content(request.getContent())
                    .contentType(request.getContentType() != null ? request.getContentType() : "text")
                    .timestamp(LocalDateTime.now())
                    .build();
            
            // Send to Kafka for persistence by MessageService
            kafkaTemplate.send(MESSAGE_TOPIC, messageDTO);
            
            log.info("Message sent to Kafka topic: {} from userId: {} to recipientId: {}", 
                    MESSAGE_TOPIC, request.getSenderId(), request.getRecipientId());
            
            return messageDTO;
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process message", e);
        }
    }
}

