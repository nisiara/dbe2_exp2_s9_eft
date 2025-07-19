package com.letrasypapeles.backend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtGenerator {
	private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	
	public String generateToken(Authentication authentication) {
		
		String username = authentication.getName();
		Date currenDate = new Date();
		Date expirationDate = new Date(System.currentTimeMillis() + 86_400_000);

		String token = Jwts.builder()
			.setSubject(username)
			.setIssuedAt(currenDate)
			.setExpiration(expirationDate)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();

		return token;
	}

	public String getUsernameFromJwtToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return claims.getSubject();
	}

	public boolean validateJwtToken(String token) {
		try{
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (Exception e){
			throw new AuthenticationCredentialsNotFoundException("El token JWT ya no es válido o ha expirado", e);
			}
		}
  
}