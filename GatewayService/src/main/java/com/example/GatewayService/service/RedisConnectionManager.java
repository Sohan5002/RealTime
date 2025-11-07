package com.example.GatewayService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisConnectionManager {

    private final RedisTemplate<String, String> redis;

    public void addConnection(String userId, String connectionId) {
        String key = "ws:user:" + userId;
        redis.opsForSet().add(key, connectionId);
        redis.expire(key, 7, TimeUnit.DAYS);
        redis.opsForValue().set("presence:user:" + userId, "online");
    }

    public void removeConnection(String userId, String connectionId) {
        String key = "ws:user:" + userId;
        redis.opsForSet().remove(key, connectionId);
        Long size = redis.opsForSet().size(key);
        if (size == null || size == 0) {
            redis.opsForValue().set("presence:user:" + userId, "offline");
        }
    }

    public Set<String> getConnections(String userId) {
        return redis.opsForSet().members("ws:user:" + userId);
    }
}
