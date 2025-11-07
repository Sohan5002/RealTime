package com.example.ChatService.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name ="messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long conversationId;
    private Long senderId;
    private String content;
    private String contentType;  // text/image/video
    private String status;       // sent/delivered/read
    private LocalDateTime createdAt = LocalDateTime.now();

}
