package org.loretdemolas.pzsw.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {JwtServiceTestConfig.class})
@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private String token;

    @BeforeEach
    public void setUp() {
        when(userDetails.getUsername()).thenReturn("testuser");
        token = jwtService.generateToken(userDetails);
    }

    @Test
    public void testExtractUsername() {
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    public void testExtractClaim() {
        String subject = jwtService.extractClaim(token, Claims::getSubject);
        assertEquals("testuser", subject);
    }

    @Test
    public void testGenerateToken() {
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void testGenerateTokenWithExtraClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ROLE_USER");

        String tokenWithClaims = jwtService.generateToken(extraClaims, userDetails);
        assertNotNull(tokenWithClaims);
        assertFalse(tokenWithClaims.isEmpty());

        Claims claims = jwtService.extractAllClaims(tokenWithClaims);
        assertEquals("ROLE_USER", ((Claims) claims).get("role"));
    }

    @Test
    public void testIsTokenValid() {
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    public void testIsTokenExpired() {
        boolean isExpired = jwtService.isTokenExpired(token);
        assertFalse(isExpired);
    }

    @Test
    public void testExtractExpiration() {
        Date expiration = jwtService.extractExpiration(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    public void testExtractAllClaims() {
        Claims claims = jwtService.extractAllClaims(token);
        assertEquals("testuser", claims.getSubject());
    }

    @Test
    public void testGetSignInKey() {
        Key key = jwtService.getSignInKey();
        assertNotNull(key);
    }

    @Test
    public void testGetExpirationTime() {
        long expirationTime = jwtService.getExpirationTime();
        assertEquals(3600000, expirationTime);
    }
}
