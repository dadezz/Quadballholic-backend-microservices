package com.quadballholic.backend.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final Logger logger  = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${quadballholic.app.jwtSecret}")
    private String jwtSecret;

    @Getter
    @Value("${quadballholic.app.AccessTokenExpirationMs:3600000}")
    private int AccessTokenExpirationMs;

    public String generateJwt(Long id, String email, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + AccessTokenExpirationMs);

        return Jwts.builder()
                .subject(email)
                .claim("roles", roles)
                .claim("id", id)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public List<GrantedAuthority> getAuthoritiesFromJwt(String token) {
        // 1. Parse the claims
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey()) // Your secret key
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 2. Extract the "roles" or "authorities" claim
        // (Ensure your Auth Service actually puts this claim into the token when creating it!)
        List<String> roles = claims.get("roles", List.class);

        if (roles == null) {
            return List.of();
        }

        // 3. Convert to Spring Security Authorities
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean validateJwt(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}