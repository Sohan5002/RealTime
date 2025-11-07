package com.example.ChatService.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;

public class KafkaConfig {
    @Bean
    public NewTopic chatMessagesTopic() {
        return new NewTopic("chat.messages", 3, (short) 1);
    }
}
