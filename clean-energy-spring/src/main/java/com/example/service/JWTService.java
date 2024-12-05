package com.example.service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTService.class);

    @Value("${jwt.secret.key}")
    private String secretKey; // Injected secret key

    @Value("${jwt.expiration.time}")
    private long jwtExpirationTime;

    /**
     * Extract the user's email from the JWT token.
     */
    public String extractUserEmail(String token) {
        try {
            String email = extractClaim(token, Claims::getSubject);
            LOGGER.debug("Extracted email from token: {}", email);
            return email;
        } catch (Exception e) {
            LOGGER.error("Error extracting user email from token: {}", e.getMessage());
            throw new RuntimeException("Error extracting user email from token", e);
        }
    }

    /**
     * Extract the expiration date from the JWT token.
     */
    public Date extractExpiration(String token) {
        try {
            Date expiration = extractClaim(token, Claims::getExpiration);
            LOGGER.debug("Extracted expiration date from token: {}", expiration);
            return expiration;
        } catch (Exception e) {
            LOGGER.error("Error extracting expiration date from token: {}", e.getMessage());
            throw new RuntimeException("Error extracting expiration date from token", e);
        }
    }

    /**
     * Extract a specific claim using a resolver function.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from the JWT token, with enhanced exception handling.
     */
    private Claims extractAllClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            LOGGER.debug("Extracted claims: {}", claims);
            return claims;
        } catch (ExpiredJwtException e) {
            LOGGER.error("Token is expired: {}", e.getMessage());
            throw new RuntimeException("Token is expired", e);
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported JWT: {}", e.getMessage());
            throw new RuntimeException("Unsupported JWT token", e);
        } catch (MalformedJwtException e) {
            LOGGER.error("Malformed JWT token: {}", e.getMessage());
            throw new RuntimeException("Malformed JWT token", e);
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT signature", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Illegal argument in JWT: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * Check if the JWT token is expired.
     */
    private Boolean isTokenExpired(String token) {
        try {
            boolean expired = extractExpiration(token).before(new Date());
            LOGGER.debug("Is token expired: {}", expired);
            return expired;
        } catch (Exception e) {
            LOGGER.error("Error checking token expiration: {}", e.getMessage());
            return true; // Treat as expired in case of any issue
        }
    }

    /**
     * Validate the JWT token against the UserDetails.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String userEmail = extractUserEmail(token);
            if (!userEmail.equalsIgnoreCase(userDetails.getUsername())) {
                LOGGER.warn("User email does not match: {} != {}", userEmail, userDetails.getUsername());
                return false;
            }
            if (isTokenExpired(token)) {
                LOGGER.warn("Token is expired");
                return false;
            }
            LOGGER.info("Token validated successfully for user: {}", userEmail);
            return true;
        } catch (Exception e) {
            LOGGER.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Generate a JWT token for a given user email.
     */
    public String generateToken(String userEmail) {
        try {
            Map<String, Object> claims = new HashMap<>();
            String token = createToken(claims, userEmail);
            LOGGER.info("Generated token for user: {}", userEmail);
            return token;
        } catch (Exception e) {
            LOGGER.error("Error generating token for user {}: {}", userEmail, e.getMessage());
            throw new RuntimeException("Error generating token", e);
        }
    }

    /**
     * Create a JWT token with claims and user email.
     */
    private String createToken(Map<String, Object> claims, String userEmail) {
        if (jwtExpirationTime <= 0) {
            LOGGER.error("Invalid JWT expiration time: {}", jwtExpirationTime);
            throw new IllegalArgumentException("JWT expiration time must be positive");
        }
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        LOGGER.debug("Created token: {}", token);
        return token;
    }

    /**
     * Get the signing key for JWT generation and validation.
     */
    private Key getSignKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            if (keyBytes.length < 32) {
                LOGGER.error("Invalid secret key length: {}", keyBytes.length);
                throw new IllegalArgumentException("Secret key must be at least 256 bits for HS256");
            }
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            LOGGER.error("Error decoding secret key: {}", e.getMessage());
            throw new RuntimeException("Error decoding secret key", e);
        }
    }
}
