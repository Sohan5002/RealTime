package com.example.PresenceService.Controller;

import com.example.PresenceService.Service.PresenceService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/presence")
@AllArgsConstructor  // Auto constructor injection for final fields
@NoArgsConstructor   // Default constructor (Spring ke internal use ke liye)
public class PresenceController {

    private PresenceService presenceService;

    // ✅ User ko online mark karna
    @PostMapping("/online/{userId}")
    public Map<String, Object> markOnline(@PathVariable Long userId) {
        presenceService.markUserOnline(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("status", "online");
        return response;
    }

    // ✅ User ko offline mark karna
    @PostMapping("/offline/{userId}")
    public Map<String, Object> markOffline(@PathVariable Long userId) {
        presenceService.markUserOffline(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("status", "offline");
        return response;
    }

    // ✅ User ka current status check karna
    @GetMapping("/status/{userId}")
    public Map<String, Object> getStatus(@PathVariable Long userId) {
        boolean isOnline = presenceService.isUserOnline(userId);
        LocalDateTime lastSeen = presenceService.getLastSeen(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("online", isOnline);
        response.put("lastSeen", lastSeen);
        return response;
    }
}
