package com.example.GatewayService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;

public class RedisTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/redis/save")
    public String save() {
        redisTemplate.opsForValue().set("user:1", "Sohan");
        return "Saved in Redis";
    }

    @GetMapping("/redis/get")
    public String get() {
        return (String) redisTemplate.opsForValue().get("user:1");
    }
}

