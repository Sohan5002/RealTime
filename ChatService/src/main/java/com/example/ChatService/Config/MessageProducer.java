package com.example.ChatService.Config;

import com.example.ChatService.DTO.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
    @Autowired
    private KafkaTemplate<String, MessageDTO> kafkaTemplate;

    public void sendAck(MessageDTO messageDTO) {

        kafkaTemplate.send("chat.status", messageDTO);
    }

}
