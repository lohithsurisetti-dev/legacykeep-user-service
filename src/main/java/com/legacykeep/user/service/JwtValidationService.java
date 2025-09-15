package com.legacykeep.user.service;

import com.legacykeep.user.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * JWT Validation Service for User Service.
 * 
 * Validates JWT tokens issued by Auth Service.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtValidationService {

    private final JwtConfig jwtConfig;

    /**
     * Get the signing key for JWT validation.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Validate and extract claims from JWT token.
     * 
     * @param token the JWT token
     * @return Optional containing claims if valid, empty otherwise
     */
    public Optional<Claims> validateAndExtractClaims(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Validate issuer and audience
            if (!jwtConfig.getIssuer().equals(claims.getIssuer())) {
                log.warn("Invalid JWT issuer: expected {}, got {}", jwtConfig.getIssuer(), claims.getIssuer());
                return Optional.empty();
            }

            // Handle audience validation - JWT audience can be a string or collection
            Object audienceClaim = claims.getAudience();
            boolean audienceValid = false;
            
            log.debug("JWT audience claim type: {}, value: {}", audienceClaim.getClass().getSimpleName(), audienceClaim);
            
            String expectedAudience = jwtConfig.getAudience();
            
            if (audienceClaim instanceof String) {
                audienceValid = expectedAudience.equals(audienceClaim);
                log.debug("String audience validation: {} == {} = {}", expectedAudience, audienceClaim, audienceValid);
            } else if (audienceClaim instanceof java.util.Collection) {
                @SuppressWarnings("unchecked")
                java.util.Collection<String> audienceCollection = (java.util.Collection<String>) audienceClaim;
                audienceValid = audienceCollection.contains(expectedAudience);
                log.debug("Collection audience validation: {} contains {} = {}", audienceCollection, expectedAudience, audienceValid);
            } else {
                // For any other type, try to convert to string and compare
                String audienceString = audienceClaim.toString();
                // Remove brackets if it's a collection string representation like "[value]"
                if (audienceString.startsWith("[") && audienceString.endsWith("]")) {
                    audienceString = audienceString.substring(1, audienceString.length() - 1);
                }
                audienceValid = expectedAudience.equals(audienceString);
                log.debug("String conversion audience validation: {} == {} = {}", expectedAudience, audienceString, audienceValid);
            }
            
            if (!audienceValid) {
                log.warn("Invalid JWT audience: expected {}, got {} (type: {})", expectedAudience, audienceClaim, audienceClaim.getClass().getSimpleName());
                return Optional.empty();
            }

            return Optional.of(claims);
        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
            return Optional.empty();
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", e.getMessage());
            return Optional.empty();
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {}", e.getMessage());
            return Optional.empty();
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            log.warn("JWT token is empty: {}", e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error validating JWT token: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Extract user ID from JWT token.
     * 
     * @param token the JWT token
     * @return Optional containing user ID if valid, empty otherwise
     */
    public Optional<Long> extractUserId(String token) {
        return validateAndExtractClaims(token)
                .map(claims -> claims.get("userId", Long.class));
    }

    /**
     * Extract email from JWT token.
     * 
     * @param token the JWT token
     * @return Optional containing email if valid, empty otherwise
     */
    public Optional<String> extractEmail(String token) {
        return validateAndExtractClaims(token)
                .map(claims -> claims.getSubject());
    }

    /**
     * Extract session ID from JWT token.
     * 
     * @param token the JWT token
     * @return Optional containing session ID if valid, empty otherwise
     */
    public Optional<String> extractSessionId(String token) {
        return validateAndExtractClaims(token)
                .map(claims -> claims.get("sessionId", String.class));
    }

    /**
     * Check if token is valid.
     * 
     * @param token the JWT token
     * @return true if valid, false otherwise
     */
    public boolean isValidToken(String token) {
        return validateAndExtractClaims(token).isPresent();
    }
}
