package br.com.imageliteapi.security;

import br.com.imageliteapi.domain.User;
import br.com.imageliteapi.service.validation.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Autowired
    private SecretKeyGenerator keyGenerator;

    public JwtService(SecretKeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public AccessToken generateToken(User user) {
        var key = keyGenerator.getKey();
        var expirationDate = generateExpirationDate();
        var claims = generateTokenClaims(user);

        String token = Jwts.builder()
                .claims(claims) // Primeiro define as claims
                .subject(user.getId().toString()) // Depois define o subject
                .expiration(expirationDate) // Define a expiração
                .signWith(key, Jwts.SIG.HS256) // Algoritmo explícito para evitar warnings
                .compact();

        return new AccessToken(token);
    }

    private Date generateExpirationDate() {
        int expirationMinutes = 60; // Pode vir de configuração externa
        LocalDateTime now = LocalDateTime.now().plusMinutes(expirationMinutes);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Map<String, Object> generateTokenClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("roles", roles);

        return claims;
    }

    public Long getIdFromToken(String tokenJwt) {
        /*
         * return Jwts.parser() .verifyWith(keyGenerator.getKey()) .build()
         * .parseSignedClaims(tokenJwt) .getPayload() .getSubject();
         */
        try {
            JwtParser build = Jwts.parser().verifyWith(keyGenerator.getKey()).build();

            Jws<Claims> jwsClaims = build.parseSignedClaims(tokenJwt);
            Claims claims = jwsClaims.getPayload();
            return Long.parseLong(claims.getSubject());
        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        }

    }
    public String getClaim(String tokenJwt, String reference) {
        /*
         * return Jwts.parser() .verifyWith(keyGenerator.getKey()) .build()
         * .parseSignedClaims(tokenJwt) .getPayload() .getSubject();
         */
        try {
            JwtParser build = Jwts.parser().verifyWith(keyGenerator.getKey()).build();

            Jws<Claims> jwsClaims = build.parseSignedClaims(tokenJwt);
            Claims claims = jwsClaims.getPayload();
            String claim = claims.get(reference, String.class);
            return claim;
        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        }catch (Exception e){
            throw new RuntimeException("invalid claim");
        }


    }
}
