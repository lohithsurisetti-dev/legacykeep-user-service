package com.legacykeep.user.controller;

import com.legacykeep.user.service.JwtValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test Controller for JWT validation testing.
 * 
 * This controller provides test endpoints to verify JWT validation functionality.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final JwtValidationService jwtValidationService;

    /**
     * Test JWT validation endpoint.
     * 
     * @param authHeader the Authorization header
     * @return test result
     */
    @GetMapping("/jwt-validation")
    public ResponseEntity<Map<String, Object>> testJwtValidation(
            @RequestHeader("Authorization") String authHeader) {
        log.info("Testing JWT validation");
        
        Map<String, Object> result = new HashMap<>();
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            result.put("success", false);
            result.put("message", "Invalid authorization header");
            return ResponseEntity.badRequest().body(result);
        }
        
        String token = authHeader.substring(7);
        
        try {
            boolean isValid = jwtValidationService.isValidToken(token);
            result.put("success", true);
            result.put("isValid", isValid);
            result.put("message", isValid ? "JWT token is valid" : "JWT token is invalid");
            
            if (isValid) {
                result.put("userId", jwtValidationService.extractUserId(token).orElse(null));
                result.put("email", jwtValidationService.extractEmail(token).orElse(null));
                result.put("sessionId", jwtValidationService.extractSessionId(token).orElse(null));
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error validating JWT token: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}
