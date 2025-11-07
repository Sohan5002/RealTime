package com.example.ChatService.Config;

import com.example.ChatService.DTO.MessageDTO;
import com.example.ChatService.Entity.Message;
import com.example.ChatService.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class  MessageConsumer {
    @Autowired
    private MessageRepository messageRepository;

    @KafkaListener(topics = "chat.messages", groupId = "chat-service")
    public void consume(MessageDTO messageDTO) {
        Message msg = new Message();
        msg.setConversationId(messageDTO.getConversationId());
        msg.setSenderId(messageDTO.getSenderId());
        msg.setContent(messageDTO.getContent());
        msg.setContentType(messageDTO.getContentType());
        msg.setStatus("SENT");
        messageRepository.save(msg);
        System.out.println("✅ Message saved in DB: " + messageDTO.getContent());
    }
}
