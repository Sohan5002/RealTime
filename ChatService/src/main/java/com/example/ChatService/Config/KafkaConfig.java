package com.example.ChatService.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Kafka Configuration
 * Creates Kafka topics if they don't exist
 */
@Configuration
public class KafkaConfig {
    
    @Bean
    public NewTopic chatMessagesTopic() {
        return new NewTopic("chat.messages", 3, (short) 1);
    }
    
    @Bean
    public NewTopic chatMessagesDeliveryTopic() {
        return new NewTopic("chat.messages.delivery", 3, (short) 1);
    }
}
