package com.legacykeep.user.controller;

import com.legacykeep.user.dto.ApiResponse;
import com.legacykeep.user.entity.UserPreferences;
import com.legacykeep.user.repository.UserPreferencesRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for User Preferences operations.
 * 
 * Handles all user preferences related endpoints.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/preferences")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Preferences Management", description = "APIs for managing user preferences")
public class UserPreferencesController {

    private final UserPreferencesRepository userPreferencesRepository;

    /**
     * Get all user preferences.
     */
    @GetMapping
    @Operation(summary = "Get all preferences", description = "Retrieve all user preferences")
    public ResponseEntity<ApiResponse<List<UserPreferences>>> getAllPreferences() {
        log.info("Retrieving all user preferences");
        
        try {
            List<UserPreferences> preferences = userPreferencesRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success(preferences, "Preferences retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving preferences", e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve preferences: " + e.getMessage()));
        }
    }

    /**
     * Get user preferences by user ID.
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get preferences by user ID", description = "Retrieve user preferences by user ID")
    public ResponseEntity<ApiResponse<UserPreferences>> getPreferencesByUserId(@PathVariable Long userId) {
        log.info("Retrieving preferences for user ID: {}", userId);
        
        try {
            Optional<UserPreferences> preferencesOpt = userPreferencesRepository.findByUserId(userId);
            if (preferencesOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Preferences not found for user ID: " + userId));
            }
            
            return ResponseEntity.ok(ApiResponse.success(preferencesOpt.get(), "Preferences retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving preferences for user ID: {}", userId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve preferences: " + e.getMessage()));
        }
    }

    /**
     * Create user preferences.
     */
    @PostMapping
    @Operation(summary = "Create user preferences", description = "Create new user preferences")
    public ResponseEntity<ApiResponse<UserPreferences>> createPreferences(@RequestBody UserPreferences preferences) {
        log.info("Creating preferences for user ID: {}", preferences.getUserId());
        
        try {
            // Check if preferences already exist for this user
            Optional<UserPreferences> existingPreferences = userPreferencesRepository.findByUserId(preferences.getUserId());
            if (existingPreferences.isPresent()) {
                return ResponseEntity.status(409)
                    .body(ApiResponse.error("Preferences already exist for user ID: " + preferences.getUserId()));
            }
            
            // Set timestamps
            preferences.setCreatedAt(LocalDateTime.now());
            preferences.setUpdatedAt(LocalDateTime.now());
            
            UserPreferences savedPreferences = userPreferencesRepository.save(preferences);
            
            log.info("Preferences created successfully with ID: {}", savedPreferences.getId());
            return ResponseEntity.status(201)
                .body(ApiResponse.success(savedPreferences, "Preferences created successfully"));
        } catch (Exception e) {
            log.error("Error creating preferences for user ID: {}", preferences.getUserId(), e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to create preferences: " + e.getMessage()));
        }
    }

    /**
     * Update user preferences.
     */
    @PutMapping("/{preferencesId}")
    @Operation(summary = "Update user preferences", description = "Update existing user preferences")
    public ResponseEntity<ApiResponse<UserPreferences>> updatePreferences(
            @PathVariable Long preferencesId, 
            @RequestBody UserPreferences updatedPreferences) {
        log.info("Updating preferences with ID: {}", preferencesId);
        
        try {
            Optional<UserPreferences> existingPreferencesOpt = userPreferencesRepository.findById(preferencesId);
            if (existingPreferencesOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Preferences not found with ID: " + preferencesId));
            }
            
            UserPreferences existingPreferences = existingPreferencesOpt.get();
            
            // Update fields (preserve ID and timestamps)
            existingPreferences.setEmailNotifications(updatedPreferences.isEmailNotifications());
            existingPreferences.setSmsNotifications(updatedPreferences.isSmsNotifications());
            existingPreferences.setPushNotifications(updatedPreferences.isPushNotifications());
            existingPreferences.setMarketingEmails(updatedPreferences.isMarketingEmails());
            existingPreferences.setProfileVisibility(updatedPreferences.getProfileVisibility());
            existingPreferences.setStoryVisibility(updatedPreferences.getStoryVisibility());
            existingPreferences.setMediaVisibility(updatedPreferences.getMediaVisibility());
            existingPreferences.setThemePreference(updatedPreferences.getThemePreference());
            existingPreferences.setFontSize(updatedPreferences.getFontSize());
            existingPreferences.setHighContrast(updatedPreferences.isHighContrast());
            existingPreferences.setReducedMotion(updatedPreferences.isReducedMotion());
            existingPreferences.setAutoPlayVideos(updatedPreferences.isAutoPlayVideos());
            existingPreferences.setDataSaverMode(updatedPreferences.isDataSaverMode());
            existingPreferences.setUpdatedAt(LocalDateTime.now());
            
            UserPreferences savedPreferences = userPreferencesRepository.save(existingPreferences);
            
            log.info("Preferences updated successfully with ID: {}", savedPreferences.getId());
            return ResponseEntity.ok(ApiResponse.success(savedPreferences, "Preferences updated successfully"));
        } catch (Exception e) {
            log.error("Error updating preferences with ID: {}", preferencesId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to update preferences: " + e.getMessage()));
        }
    }

    /**
     * Delete user preferences.
     */
    @DeleteMapping("/{preferencesId}")
    @Operation(summary = "Delete user preferences", description = "Delete user preferences")
    public ResponseEntity<ApiResponse<Void>> deletePreferences(@PathVariable Long preferencesId) {
        log.info("Deleting preferences with ID: {}", preferencesId);
        
        try {
            Optional<UserPreferences> preferencesOpt = userPreferencesRepository.findById(preferencesId);
            if (preferencesOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Preferences not found with ID: " + preferencesId));
            }
            
            userPreferencesRepository.deleteById(preferencesId);
            
            log.info("Preferences deleted successfully with ID: {}", preferencesId);
            return ResponseEntity.ok(ApiResponse.success(null, "Preferences deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting preferences with ID: {}", preferencesId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to delete preferences: " + e.getMessage()));
        }
    }
}
