package com.myapp.usermanagement.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

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
}
