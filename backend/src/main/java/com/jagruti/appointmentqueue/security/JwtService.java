package com.jagruti.appointmentqueue.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // 🔐 MUST be at least 32 characters for HS256
    private static final String SECRET_KEY =
            "appointmentQueueSecretKeyappointmentQueueSecretKey";

    // ⏳ 24 hours
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // ================================
    // 🔑 Internal signing key
    // ================================
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ================================
    // 🔐 Generate JWT Token
    // ================================
    public String generateToken(String email, String role) {

        return Jwts.builder()
                .setSubject(email)                 // username / email
                .claim("role", role)               // ROLE_USER / ROLE_ADMIN
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ================================
    // 📩 Extract Email (subject)
    // ================================
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ================================
    // 🎭 Extract Role
    // ================================
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ================================
    // ✅ Validate Token
    // ================================
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // ================================
    // 🔍 Parse claims
    // ================================
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
