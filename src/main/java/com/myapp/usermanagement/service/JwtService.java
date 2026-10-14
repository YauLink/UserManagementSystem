package com.myapp.usermanagement.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {

        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET must be configured");
        }

        if (expiration <= 0) {
            throw new IllegalStateException("JWT expiration must be positive");
        }

        this.signingKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );

        this.expiration = expiration;
    }

    public String generateToken(
            Long userId,
            String username,
            String fullName,
            String role) {

        Date issuedAt = new Date();
        Date expirationDate =
                new Date(issuedAt.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("fullName", fullName)
                .claim("role", role)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {

        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    public boolean isTokenValid(String token) {

        try {
            Claims claims = extractAllClaims(token);

            if (claims.getSubject() == null
                    || claims.get("userId", Long.class) == null
                    || claims.get("fullName", String.class) == null
                    || claims.get("role", String.class) == null) {
                return false;
            }

            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
