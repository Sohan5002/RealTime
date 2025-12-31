package com.example.MessageService.service;

import com.example.MessageService.dto.MessageDTO;
import com.example.MessageService.entity.Message;
import com.example.MessageService.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Message Service
 * Handles message persistence and Kafka publishing
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String DELIVERY_TOPIC = "chat.messages.delivery";

    /**
     * Save message to database
     */
    public Message saveMessage(MessageDTO messageDTO) {
        try {
            Message message = new Message();
            message.setSenderId(messageDTO.getSenderId());
            message.setRecipientId(messageDTO.getRecipientId());
            message.setContent(messageDTO.getContent());
            message.setContentType(messageDTO.getContentType() != null ? messageDTO.getContentType() : "text");
            message.setStatus("SENT");
            if (messageDTO.getTimestamp() != null) {
                message.setCreatedAt(messageDTO.getTimestamp());
            }

            Message saved = messageRepository.save(message);
            log.info("Message saved to database: id={}, senderId={}, recipientId={}", 
                    saved.getId(), saved.getSenderId(), saved.getRecipientId());

            // Publish delivery notification to Kafka for real-time delivery
            publishDeliveryNotification(saved);

            return saved;
        } catch (Exception e) {
            log.error("Error saving message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save message", e);
        }
    }

    /**
     * Publish delivery notification to Kafka
     */
    private void publishDeliveryNotification(Message message) {
        try {
            MessageDTO deliveryDTO = MessageDTO.builder()
                    .id(message.getId())
                    .senderId(message.getSenderId())
                    .recipientId(message.getRecipientId())
                    .content(message.getContent())
                    .contentType(message.getContentType())
                    .status(message.getStatus())
                    .timestamp(message.getCreatedAt())
                    .build();

            kafkaTemplate.send(DELIVERY_TOPIC, String.valueOf(message.getRecipientId()), deliveryDTO);
            log.info("Delivery notification published to Kafka for recipientId: {}", message.getRecipientId());
        } catch (Exception e) {
            log.error("Error publishing delivery notification: {}", e.getMessage(), e);
            // Don't throw exception - message is already saved
        }
    }
}

