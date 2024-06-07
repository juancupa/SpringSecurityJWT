package com.example.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {
	
	
	@Value("${jwt.secret.key}")
	private String secretKey;
	
	@Value("${jwt.time.expiration}")
	private String timeExpiration;
	
	
	//Generate token de access
	
	public String generateAccessToken(String username) {
		
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
				.signWith(getSignatureKey(),SignatureAlgorithm.HS256)
				.compact();
				
	}
	
	
	//Obtener firma del token
	
	public Key getSignatureKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
		
	}
	
	//valida el token de acceso
	public boolean isTokenValid(String token) {
		
		try {
			
			Jwts.parser()
				.setSigningKey(getSignatureKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
			
			return true;
		} catch (Exception e) {
			
			log.error("Token invaido error: ".concat(e.toString()));
			return false;
			
		}
	}
	
	//obtener todos los claims del token
	public Claims extractAllClaims(String token) {
		
	return	Jwts.parser()
		.setSigningKey(getSignatureKey())
		.build()
		.parseClaimsJws(token)
		.getBody();
	
	}
	
	//obtener solo un claim
	public <T> T getClaim(String token, Function<Claims, T> claimsFunction) {
		
		Claims claims = extractAllClaims(token);
		return claimsFunction.apply(claims);
	}
	
	//obtener el user del token
	
	public String getUsernameFromToken(String token) {
		
		return getClaim(token, Claims :: getSubject);
	}

}
