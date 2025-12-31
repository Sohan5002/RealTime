package com.example.MessageService.repository;

import com.example.MessageService.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Message Repository
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    /**
     * Find messages between two users
     */
    List<Message> findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByCreatedAtAsc(
            Long senderId1, Long recipientId1, Long senderId2, Long recipientId2);
    
    /**
     * Find messages by sender
     */
    List<Message> findBySenderId(Long senderId);
    
    /**
     * Find messages by recipient
     */
    List<Message> findByRecipientId(Long recipientId);
}

