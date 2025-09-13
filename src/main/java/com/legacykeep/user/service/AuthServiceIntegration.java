package com.legacykeep.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Service for integrating with Auth Service.
 * 
 * Validates users and retrieves user information from the Auth Service.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceIntegration {

    private final RestTemplate restTemplate;

    @Value("${user.auth-service.url:http://localhost:8081}")
    private String authServiceUrl;

    /**
     * Validate user existence with Auth Service.
     * 
     * @param userId the user ID to validate
     * @return true if user exists, false otherwise
     */
    public boolean validateUserExists(Long userId) {
        try {
            String url = authServiceUrl + "/api/v1/users/" + userId + "/exists";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                return Boolean.TRUE.equals(body.get("exists"));
            }
            
            return false;
        } catch (Exception e) {
            log.error("Error validating user existence for user ID: {}", userId, e);
            return false;
        }
    }

    /**
     * Get user information from Auth Service.
     * 
     * @param userId the user ID
     * @return user information map or null if not found
     */
    public Map<String, Object> getUserInfo(Long userId) {
        try {
            String url = authServiceUrl + "/api/v1/users/" + userId;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
            
            return null;
        } catch (Exception e) {
            log.error("Error getting user info for user ID: {}", userId, e);
            return null;
        }
    }

    /**
     * Validate JWT token with Auth Service.
     * 
     * @param token the JWT token
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            String url = authServiceUrl + "/api/v1/auth/validate";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, Map.class);
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error validating token", e);
            return false;
        }
    }

    /**
     * Get user ID from JWT token via Auth Service.
     * 
     * @param token the JWT token
     * @return user ID or null if invalid
     */
    public Long getUserIdFromToken(String token) {
        try {
            String url = authServiceUrl + "/api/v1/auth/user-id";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                Object userIdObj = body.get("userId");
                if (userIdObj instanceof Number) {
                    return ((Number) userIdObj).longValue();
                }
            }
            
            return null;
        } catch (Exception e) {
            log.error("Error getting user ID from token", e);
            return null;
        }
    }
}
