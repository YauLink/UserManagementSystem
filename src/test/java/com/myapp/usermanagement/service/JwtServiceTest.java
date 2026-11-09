package com.myapp.usermanagement.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET = System.getProperty("JWT_SECRET");

    @Test
    void generatesTokenWithAirportContractClaims() {
        JwtService service = new JwtService(SECRET, 3_600_000L);

        String token = service.generateToken(
                7L,
                "john",
                "John Doe",
                "USER"
        );

        Claims claims = service.extractAllClaims(token);

        assertEquals("john", claims.getSubject());
        assertEquals(7L, claims.get("userId", Long.class));
        assertEquals("John Doe", claims.get("fullName", String.class));
        assertEquals("USER", claims.get("role", String.class));
        assertTrue(service.isTokenValid(token));
    }

    @Test
    void rejectsNonPositiveExpirationConfiguration() {
        assertThrows(IllegalStateException.class,
                () -> new JwtService(SECRET, -1L));
    }

    @Test
    void rejectsExpiredTokens() {
        JwtService service = new JwtService(SECRET, 3_600_000L);
        Date now = new Date();
        String expired = Jwts.builder()
                .setSubject("john")
                .claim("userId", 7L)
                .claim("fullName", "John Doe")
                .claim("role", "USER")
                .setIssuedAt(new Date(now.getTime() - 2_000L))
                .setExpiration(new Date(now.getTime() - 1_000L))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET)), SignatureAlgorithm.HS256)
                .compact();

        assertFalse(service.isTokenValid(expired));
    }

    @Test
    void rejectsTokensMissingRequiredClaims() {
        JwtService service = new JwtService(SECRET, 3_600_000L);
        String incomplete = Jwts.builder()
                .setSubject("john")
                .claim("userId", 7L)
                .claim("fullName", "John Doe")
                .setExpiration(new Date(System.currentTimeMillis() + 3_600_000L))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET)), SignatureAlgorithm.HS256)
                .compact();

        assertFalse(service.isTokenValid(incomplete));
    }
}
