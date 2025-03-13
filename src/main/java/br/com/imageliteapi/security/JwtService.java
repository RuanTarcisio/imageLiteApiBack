package br.com.imageliteapi.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.service.validation.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

	@Autowired
	private SecretKeyGenerator keyGenerator;

	public AccessToken generateToken(User user) {

		var key = keyGenerator.getKey();
		var expirationDate = generateExpirationDate();
		var claims = generateTokenClaims(user);

		String token = Jwts.builder().signWith(key).subject(user.getEmail()).expiration(expirationDate).claims(claims)
				.compact();

		return new AccessToken(token);
	}

	private Date generateExpirationDate() {
		var expirationMinutes = 60;
		LocalDateTime now = LocalDateTime.now().plusMinutes(expirationMinutes);
		return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
	}

	private Map<String, Object> generateTokenClaims(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("name", user.getName());
		claims.put("id", user.getId());
//		claims.put("name", claims);  ADICIONAR MAIS INFORMACOES
		return claims;
	}

	public String getEmailFromToken(String tokenJwt) {
		/*
		 * return Jwts.parser() .verifyWith(keyGenerator.getKey()) .build()
		 * .parseSignedClaims(tokenJwt) .getPayload() .getSubject();
		 */
		try {
			JwtParser build = Jwts.parser().verifyWith(keyGenerator.getKey()).build();

			Jws<Claims> jwsClaims = build.parseSignedClaims(tokenJwt);
			Claims claims = jwsClaims.getPayload();
			return claims.getSubject();
		} catch (JwtException e) {
			throw new InvalidTokenException(e.getMessage());
		}

	}
}
