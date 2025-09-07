package com.legacykeep.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT Configuration properties for User Service.
 * 
 * Manages JWT token validation settings to work with Auth Service.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "user.jwt")
public class JwtConfig {

    /**
     * JWT secret key for validating tokens (shared with Auth Service).
     */
    private String secret = "legacykeep-jwt-secret-key-change-in-production-512-bits-minimum-required-for-hs512-algorithm";

    /**
     * JWT issuer claim.
     */
    private String issuer = "LegacyKeep";

    /**
     * JWT audience claim.
     */
    private String audience = "LegacyKeep-Users";

    /**
     * JWT algorithm for validation.
     */
    private String algorithm = "HS256";

    /**
     * Access token expiration time in milliseconds.
     */
    private long accessTokenExpiration = 900000; // 15 minutes

    /**
     * Refresh token expiration time in milliseconds.
     */
    private long refreshTokenExpiration = 604800000; // 7 days
}
