package com.legacykeep.user.controller;

import com.legacykeep.user.dto.ApiResponse;
import com.legacykeep.user.entity.UserActivityLog;
import com.legacykeep.user.repository.UserActivityLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for User Activity operations.
 * 
 * Handles all user activity logging related endpoints.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/activity-logs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Activity Management", description = "APIs for managing user activity logs")
public class UserActivityController {

    private final UserActivityLogRepository userActivityLogRepository;

    /**
     * Get all user activity logs.
     */
    @GetMapping
    @Operation(summary = "Get all activity logs", description = "Retrieve all user activity logs")
    public ResponseEntity<ApiResponse<List<UserActivityLog>>> getAllActivityLogs() {
        log.info("Retrieving all user activity logs");
        
        try {
            List<UserActivityLog> activityLogs = userActivityLogRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success(activityLogs, "Activity logs retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving activity logs", e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve activity logs: " + e.getMessage()));
        }
    }

    /**
     * Get user activity logs by user ID.
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get activity logs by user ID", description = "Retrieve user activity logs by user ID")
    public ResponseEntity<ApiResponse<List<UserActivityLog>>> getActivityLogsByUserId(@PathVariable Long userId) {
        log.info("Retrieving activity logs for user ID: {}", userId);
        
        try {
            List<UserActivityLog> activityLogs = userActivityLogRepository.findByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success(activityLogs, "Activity logs retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving activity logs for user ID: {}", userId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve activity logs: " + e.getMessage()));
        }
    }

    /**
     * Log user activity.
     */
    @PostMapping
    @Operation(summary = "Log user activity", description = "Log a new user activity")
    public ResponseEntity<ApiResponse<UserActivityLog>> logActivity(@RequestBody UserActivityLog activityLog) {
        log.info("Logging activity for user ID: {}", activityLog.getUserId());
        
        try {
            // Set timestamps
            activityLog.setCreatedAt(LocalDateTime.now());
            
            UserActivityLog savedActivityLog = userActivityLogRepository.save(activityLog);
            
            log.info("Activity logged successfully with ID: {}", savedActivityLog.getId());
            return ResponseEntity.status(201)
                .body(ApiResponse.success(savedActivityLog, "Activity logged successfully"));
        } catch (Exception e) {
            log.error("Error logging activity for user ID: {}", activityLog.getUserId(), e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to log activity: " + e.getMessage()));
        }
    }

    /**
     * Get activity logs by activity type.
     */
    @GetMapping("/type/{activityType}")
    @Operation(summary = "Get activity logs by type", description = "Retrieve activity logs of a specific type")
    public ResponseEntity<ApiResponse<List<UserActivityLog>>> getActivityLogsByType(@PathVariable String activityType) {
        log.info("Retrieving activity logs of type: {}", activityType);
        
        try {
            List<UserActivityLog> activityLogs = userActivityLogRepository.findAll()
                .stream()
                .filter(log -> activityType.equals(log.getActivityType()))
                .toList();
            
            return ResponseEntity.ok(ApiResponse.success(activityLogs, "Activity logs retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving activity logs of type: {}", activityType, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve activity logs: " + e.getMessage()));
        }
    }

    /**
     * Get recent activity logs for a user.
     */
    @GetMapping("/user/{userId}/recent")
    @Operation(summary = "Get recent activity logs", description = "Get recent activity logs for a user")
    public ResponseEntity<ApiResponse<List<UserActivityLog>>> getRecentActivityLogs(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        log.info("Retrieving recent {} activity logs for user ID: {}", limit, userId);
        
        try {
            List<UserActivityLog> activityLogs = userActivityLogRepository.findByUserId(userId)
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(limit)
                .toList();
            
            return ResponseEntity.ok(ApiResponse.success(activityLogs, "Recent activity logs retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving recent activity logs for user ID: {}", userId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve recent activity logs: " + e.getMessage()));
        }
    }
}
