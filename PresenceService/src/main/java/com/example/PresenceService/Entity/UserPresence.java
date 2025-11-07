package com.example.PresenceService.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_presence")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPresence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;              // kis user ka presence track ho raha hai
    private boolean online;           // online ya offline
    private LocalDateTime lastSeen;   // last active time
}
