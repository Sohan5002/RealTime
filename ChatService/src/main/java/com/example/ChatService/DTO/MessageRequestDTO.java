package com.example.ChatService.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for incoming message requests via WebSocket
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDTO {
    private Long senderId; // Set by controller from principal
    private Long recipientId;
    private String content;
    private String contentType = "text";
}

