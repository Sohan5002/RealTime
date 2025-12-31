package com.example.MessageService.controller;

import com.example.MessageService.entity.Message;
import com.example.MessageService.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Message Controller
 * REST endpoints for message retrieval
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageRepository messageRepository;

    /**
     * Get messages between two users
     */
    @GetMapping("/between/{userId1}/{userId2}")
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(
            @PathVariable Long userId1,
            @PathVariable Long userId2) {
        List<Message> messages = messageRepository
                .findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByCreatedAtAsc(
                        userId1, userId2, userId2, userId1);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get messages by sender
     */
    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<Message>> getSentMessages(@PathVariable Long senderId) {
        List<Message> messages = messageRepository.findBySenderId(senderId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get messages by recipient
     */
    @GetMapping("/received/{recipientId}")
    public ResponseEntity<List<Message>> getReceivedMessages(@PathVariable Long recipientId) {
        List<Message> messages = messageRepository.findByRecipientId(recipientId);
        return ResponseEntity.ok(messages);
    }
}

