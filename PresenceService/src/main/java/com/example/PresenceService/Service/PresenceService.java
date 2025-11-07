package com.example.PresenceService.Service;




import com.example.PresenceService.Entity.UserPresence;
import com.example.PresenceService.Repository.UserPresenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PresenceService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserPresenceRepository presenceRepository;

    private static final String REDIS_KEY_PREFIX = "presence:user:";

    // ✅ User Online hone par call
    public void markUserOnline(Long userId) {
        String key = REDIS_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, "online");

        Optional<UserPresence> existing = presenceRepository.findByUserId(userId);
        UserPresence presence = existing.orElse(new UserPresence(null, userId, true, LocalDateTime.now()));
        presence.setOnline(true);
        presence.setLastSeen(LocalDateTime.now());
        presenceRepository.save(presence);
    }

    // ✅ User Offline hone par call
    public void markUserOffline(Long userId) {
        String key = REDIS_KEY_PREFIX + userId;
        redisTemplate.delete(key);

        presenceRepository.findByUserId(userId).ifPresent(presence -> {
            presence.setOnline(false);
            presence.setLastSeen(LocalDateTime.now());
            presenceRepository.save(presence);
        });
    }

    // ✅ Redis se online status check karna
    public boolean isUserOnline(Long userId) {
        String key = REDIS_KEY_PREFIX + userId;
        Object status = redisTemplate.opsForValue().get(key);
        return status != null && status.equals("online");
    }

    // ✅ MySQL se last seen time check karna
    public LocalDateTime getLastSeen(Long userId) {
        return presenceRepository.findByUserId(userId)
                .map(UserPresence::getLastSeen)
                .orElse(null);
    }
}
