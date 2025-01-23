package com.demo.launchpad.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtManager {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifespan}")
    private long lifespan;

    private SecretKey generateSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(CustomUserDetails principal) {
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
        claims.put("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList());
        claims.put("firstname", principal.getFirstname());
        claims.put("lastname", principal.getLastname());


        return Jwts.builder()
                .claims(claims)
                .issuer("quartermaster")
                .subject(principal.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + lifespan))
                .signWith(generateSecretKey())
                .compact();
    }

    private Claims getClaims(String token) {
        JwtParser parser = Jwts.parser().verifyWith(generateSecretKey()).build();
        return parser.parseSignedClaims(token).getPayload();
    }

    public String getRole(String token) {
        return (String) getClaims(token).get("role");
    }

    public String extractUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }
}
