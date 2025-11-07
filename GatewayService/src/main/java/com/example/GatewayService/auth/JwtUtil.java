package com.example.GatewayService.auth;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Map;

@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${ws.jwt.secret}") String secret) {
        // simple key from secret (for demo). Use proper key management in prod.
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public String getUserId(String token) {
        Jws<Claims> jws = parseToken(token);
        return String.valueOf(jws.getBody().get("userId"));
    }
}
