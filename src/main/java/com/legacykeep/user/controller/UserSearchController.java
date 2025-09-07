package com.legacykeep.user.controller;

import com.legacykeep.user.dto.ApiResponse;
import com.legacykeep.user.entity.UserActivityLog;
import com.legacykeep.user.entity.UserInterest;
import com.legacykeep.user.entity.UserPreferences;
import com.legacykeep.user.entity.UserProfile;
import com.legacykeep.user.entity.UserSettings;
import com.legacykeep.user.repository.UserActivityLogRepository;
import com.legacykeep.user.repository.UserInterestRepository;
import com.legacykeep.user.repository.UserPreferencesRepository;
import com.legacykeep.user.repository.UserProfileRepository;
import com.legacykeep.user.repository.UserSettingsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for User Search and Utility operations.
 * 
 * Handles search, dashboard, and utility endpoints.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Search & Utilities", description = "APIs for user search, dashboard, and utilities")
public class UserSearchController {

    private final UserProfileRepository userProfileRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final UserInterestRepository userInterestRepository;
    private final UserActivityLogRepository userActivityLogRepository;
    private final UserSettingsRepository userSettingsRepository;

    /**
     * Get service health status.
     */
    @GetMapping("/health")
    @Operation(summary = "Get service health", description = "Simple health check endpoint")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHealth() {
        log.info("Health check requested");
        
        var healthData = Map.<String, Object>of(
            "status", "UP",
            "service", "User Service",
            "version", "1.0.0",
            "timestamp", LocalDateTime.now().toString(),
            "database", "Connected"
        );
        
        return ResponseEntity.ok(ApiResponse.success(healthData, "Service is healthy"));
    }

    /**
     * Get current authenticated user info.
     */
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get information about the currently authenticated user")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser() {
        log.info("Getting current user information");
        
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                var userInfo = Map.<String, Object>of(
                    "email", auth.getName(),
                    "userId", auth.getDetails(),
                    "authorities", auth.getAuthorities(),
                    "authenticated", true,
                    "timestamp", LocalDateTime.now().toString()
                );
                return ResponseEntity.ok(ApiResponse.success(userInfo, "Current user information retrieved successfully"));
            } else {
                return ResponseEntity.status(401)
                    .body(ApiResponse.error("User not authenticated"));
            }
        } catch (Exception e) {
            log.error("Error getting current user information", e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to get current user information: " + e.getMessage()));
        }
    }

    /**
     * Search users by name.
     */
    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Search users by name or display name")
    public ResponseEntity<ApiResponse<List<UserProfile>>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        log.info("Searching users with query: {}", query);
        
        try {
            List<UserProfile> profiles = userProfileRepository.findAll()
                .stream()
                .filter(profile -> 
                    (profile.getFirstName() != null && profile.getFirstName().toLowerCase().contains(query.toLowerCase())) ||
                    (profile.getLastName() != null && profile.getLastName().toLowerCase().contains(query.toLowerCase())) ||
                    (profile.getDisplayName() != null && profile.getDisplayName().toLowerCase().contains(query.toLowerCase()))
                )
                .limit(limit)
                .toList();
            
            return ResponseEntity.ok(ApiResponse.success(profiles, "Search results retrieved successfully"));
        } catch (Exception e) {
            log.error("Error searching users with query: {}", query, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to search users: " + e.getMessage()));
        }
    }

    /**
     * Get user dashboard data.
     */
    @GetMapping("/dashboard/{userId}")
    @Operation(summary = "Get user dashboard", description = "Get comprehensive user dashboard data")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserDashboard(@PathVariable Long userId) {
        log.info("Retrieving dashboard data for user ID: {}", userId);
        
        try {
            // Get user profile
            Optional<UserProfile> profileOpt = userProfileRepository.findByUserId(userId);
            if (profileOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("User not found with ID: " + userId));
            }
            
            // Get user preferences
            Optional<UserPreferences> preferencesOpt = userPreferencesRepository.findByUserId(userId);
            
            // Get user settings
            Optional<UserSettings> settingsOpt = userSettingsRepository.findByUserId(userId);
            
            // Get user interests
            List<UserInterest> interests = userInterestRepository.findByUserId(userId);
            
            // Get recent activity
            List<UserActivityLog> recentActivity = userActivityLogRepository.findByUserId(userId)
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .toList();
            
            var dashboardData = Map.<String, Object>of(
                "profile", profileOpt.get(),
                "preferences", preferencesOpt.orElse(null),
                "settings", settingsOpt.orElse(null),
                "interests", interests,
                "recentActivity", recentActivity,
                "timestamp", LocalDateTime.now().toString()
            );
            
            return ResponseEntity.ok(ApiResponse.success(dashboardData, "Dashboard data retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving dashboard data for user ID: {}", userId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve dashboard data: " + e.getMessage()));
        }
    }

    /**
     * Get database statistics.
     */
    @GetMapping("/stats")
    @Operation(summary = "Get database stats", description = "Retrieve database statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDatabaseStats() {
        log.info("Retrieving database statistics");
        
        try {
            long totalProfiles = userProfileRepository.count();
            long totalPreferences = userPreferencesRepository.count();
            long totalSettings = userSettingsRepository.count();
            long totalInterests = userInterestRepository.count();
            long totalActivityLogs = userActivityLogRepository.count();
            
            var stats = Map.<String, Object>of(
                "totalProfiles", totalProfiles,
                "totalPreferences", totalPreferences,
                "totalSettings", totalSettings,
                "totalInterests", totalInterests,
                "totalActivityLogs", totalActivityLogs,
                "timestamp", LocalDateTime.now().toString(),
                "database", "PostgreSQL",
                "status", "Connected"
            );
            
            return ResponseEntity.ok(ApiResponse.success(stats, "Database statistics retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving database statistics", e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve statistics: " + e.getMessage()));
        }
    }
}


