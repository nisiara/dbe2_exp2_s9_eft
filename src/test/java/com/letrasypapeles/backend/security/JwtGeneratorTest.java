package com.letrasypapeles.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
// import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;

import io.jsonwebtoken.security.Keys;

// @ExtendWith(MockitoExtension.class)
// public class JwtGeneratorTest {
  
//   private JwtGenerator jwtGenerator;

//   @Mock
//   private Authentication authentication;

//   @BeforeEach
//     public void setUp() {
//       jwtGenerator = new JwtGenerator();
//     }

//     @Test
//     public void generarToken() {
//       String username = "testuser";
//       when(authentication.getName()).thenReturn(username);

//       String token = jwtGenerator.generateToken(authentication);

//       assertNotNull(token);
//       assertFalse(token.isEmpty());
//   }

//   @Test
//   public void obtenerUsuario() {
    
//     String username = "testuser";
//     when(authentication.getName()).thenReturn(username);

//     String token = jwtGenerator.generateToken(authentication);
//     String extractedUsername = jwtGenerator.getUsernameFromJwtToken(token);

//     assertNotNull(extractedUsername);
//     assertEquals(username, extractedUsername);
//   }

//   @Test
//   public void validarToken() {
    
//     String username = "validuser";
//     when(authentication.getName()).thenReturn(username);

//     String validToken = jwtGenerator.generateToken(authentication);

//     boolean isValid = jwtGenerator.validateJwtToken(validToken);
//     assertTrue(isValid);
//   }

//   @Test
//   public void tokenExpirado() throws InterruptedException {
//     String username = "expireduser";
//     long shortExpiryMs = 100;
//     Date issuedAt = new Date();
//     Date expirationDate = new Date(issuedAt.getTime() + shortExpiryMs);

//     String expiredToken = Jwts.builder()
//     .setSubject(username)
//     .setIssuedAt(issuedAt)
//     .setExpiration(expirationDate)
//     .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512), SignatureAlgorithm.HS512)
//     .compact();
    
//     Thread.sleep(shortExpiryMs + 50);

//     AuthenticationCredentialsNotFoundException thrown = assertThrows(
//       AuthenticationCredentialsNotFoundException.class,
//       () -> jwtGenerator.validateJwtToken(expiredToken)
//     );

//     assertTrue(thrown.getMessage().contains("El token JWT ya no es válido o ha expirado"));
        
//   }

// }
