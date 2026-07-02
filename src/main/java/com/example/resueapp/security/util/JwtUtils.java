package com.example.resueapp.security.util;

import com.example.resueapp.security.model.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtUtils(@Value("${jwt-secret}") String secret, @Value("${jwt-expiration-ms}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.expirationMs = expirationMs;
    }

    /**
     * Generates a signed JWT token containing the user's email and security role.
     */
    public String generateToken(CustomUserPrincipal user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("role", user.getRole().name());
        claims.put("firstLogin", user.isFirstLogin());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getPhone())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey) // Sign the token mathematically securely
                .compact();
    }

    /**
     * Extracts the user's phone (Subject) from an incoming token.
     */
    public String extractPhone(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    /**
     * Extracts the custom user role from an incoming token.
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public Boolean isFirstLogin(String token) {
        return extractAllClaims(token).get("firstLogin", Boolean.class);
    }

    /**
     * Checks if the token has expired.
     */
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Helper method to parse the encrypted token layers safely
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}