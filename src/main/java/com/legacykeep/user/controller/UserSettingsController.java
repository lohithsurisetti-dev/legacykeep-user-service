package com.legacykeep.user.controller;

import com.legacykeep.user.dto.ApiResponse;
import com.legacykeep.user.entity.UserSettings;
import com.legacykeep.user.repository.UserSettingsRepository;
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
 * REST Controller for User Settings operations.
 * 
 * Handles all user settings related endpoints including security,
 * privacy, and account management settings.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Settings Management", description = "APIs for managing user account and security settings")
public class UserSettingsController {

    private final UserSettingsRepository userSettingsRepository;

    /**
     * Get all user settings.
     */
    @GetMapping
    @Operation(summary = "Get all user settings", description = "Retrieve all user settings")
    public ResponseEntity<ApiResponse<List<UserSettings>>> getAllSettings() {
        log.info("Retrieving all user settings");
        
        try {
            List<UserSettings> settings = userSettingsRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success(settings, "User settings retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving user settings", e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve user settings: " + e.getMessage()));
        }
    }

    /**
     * Get user settings by user ID.
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get settings by user ID", description = "Retrieve user settings by user ID")
    public ResponseEntity<ApiResponse<UserSettings>> getSettingsByUserId(@PathVariable Long userId) {
        log.info("Retrieving settings for user ID: {}", userId);
        
        try {
            Optional<UserSettings> settingsOpt = userSettingsRepository.findByUserId(userId);
            if (settingsOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Settings not found for user ID: " + userId));
            }
            
            return ResponseEntity.ok(ApiResponse.success(settingsOpt.get(), "Settings retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving settings for user ID: {}", userId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve settings: " + e.getMessage()));
        }
    }

    /**
     * Create user settings.
     */
    @PostMapping
    @Operation(summary = "Create user settings", description = "Create new user settings")
    public ResponseEntity<ApiResponse<UserSettings>> createSettings(@RequestBody UserSettings settings) {
        log.info("Creating settings for user ID: {}", settings.getUserId());
        
        try {
            // Check if settings already exist for this user
            Optional<UserSettings> existingSettings = userSettingsRepository.findByUserId(settings.getUserId());
            if (existingSettings.isPresent()) {
                return ResponseEntity.status(409)
                    .body(ApiResponse.error("Settings already exist for user ID: " + settings.getUserId()));
            }
            
            // Set timestamps
            settings.setCreatedAt(LocalDateTime.now());
            settings.setUpdatedAt(LocalDateTime.now());
            
            UserSettings savedSettings = userSettingsRepository.save(settings);
            
            log.info("Settings created successfully with ID: {}", savedSettings.getId());
            return ResponseEntity.status(201)
                .body(ApiResponse.success(savedSettings, "Settings created successfully"));
        } catch (Exception e) {
            log.error("Error creating settings for user ID: {}", settings.getUserId(), e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to create settings: " + e.getMessage()));
        }
    }

    /**
     * Update user settings.
     */
    @PutMapping("/{settingsId}")
    @Operation(summary = "Update user settings", description = "Update existing user settings")
    public ResponseEntity<ApiResponse<UserSettings>> updateSettings(
            @PathVariable Long settingsId, 
            @RequestBody UserSettings updatedSettings) {
        log.info("Updating settings with ID: {}", settingsId);
        
        try {
            Optional<UserSettings> existingSettingsOpt = userSettingsRepository.findById(settingsId);
            if (existingSettingsOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Settings not found with ID: " + settingsId));
            }
            
            UserSettings existingSettings = existingSettingsOpt.get();
            
            // Update fields (preserve ID and timestamps)
            existingSettings.setTwoFactorEnabled(updatedSettings.isTwoFactorEnabled());
            existingSettings.setLoginNotifications(updatedSettings.isLoginNotifications());
            existingSettings.setDeviceManagement(updatedSettings.isDeviceManagement());
            existingSettings.setDataExportEnabled(updatedSettings.isDataExportEnabled());
            existingSettings.setAccountDeletionScheduledAt(updatedSettings.getAccountDeletionScheduledAt());
            existingSettings.setLastPasswordChangeAt(updatedSettings.getLastPasswordChangeAt());
            existingSettings.setLastProfileUpdateAt(updatedSettings.getLastProfileUpdateAt());
            existingSettings.setPrivacyLevel(updatedSettings.getPrivacyLevel());
            existingSettings.setDataRetentionDays(updatedSettings.getDataRetentionDays());
            existingSettings.setUpdatedAt(LocalDateTime.now());
            
            UserSettings savedSettings = userSettingsRepository.save(existingSettings);
            
            log.info("Settings updated successfully with ID: {}", savedSettings.getId());
            return ResponseEntity.ok(ApiResponse.success(savedSettings, "Settings updated successfully"));
        } catch (Exception e) {
            log.error("Error updating settings with ID: {}", settingsId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to update settings: " + e.getMessage()));
        }
    }

    /**
     * Delete user settings.
     */
    @DeleteMapping("/{settingsId}")
    @Operation(summary = "Delete user settings", description = "Delete user settings")
    public ResponseEntity<ApiResponse<Void>> deleteSettings(@PathVariable Long settingsId) {
        log.info("Deleting settings with ID: {}", settingsId);
        
        try {
            Optional<UserSettings> settingsOpt = userSettingsRepository.findById(settingsId);
            if (settingsOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Settings not found with ID: " + settingsId));
            }
            
            userSettingsRepository.deleteById(settingsId);
            
            log.info("Settings deleted successfully with ID: {}", settingsId);
            return ResponseEntity.ok(ApiResponse.success(null, "Settings deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting settings with ID: {}", settingsId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to delete settings: " + e.getMessage()));
        }
    }

    /**
     * Update two-factor authentication setting.
     */
    @PutMapping("/user/{userId}/two-factor")
    @Operation(summary = "Update 2FA setting", description = "Enable or disable two-factor authentication")
    public ResponseEntity<ApiResponse<UserSettings>> updateTwoFactorSetting(
            @PathVariable Long userId,
            @RequestParam boolean enabled) {
        log.info("Updating 2FA setting for user ID: {} to: {}", userId, enabled);
        
        try {
            Optional<UserSettings> settingsOpt = userSettingsRepository.findByUserId(userId);
            if (settingsOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Settings not found for user ID: " + userId));
            }
            
            UserSettings settings = settingsOpt.get();
            settings.setTwoFactorEnabled(enabled);
            settings.setUpdatedAt(LocalDateTime.now());
            
            UserSettings savedSettings = userSettingsRepository.save(settings);
            
            log.info("2FA setting updated successfully for user ID: {}", userId);
            return ResponseEntity.ok(ApiResponse.success(savedSettings, "2FA setting updated successfully"));
        } catch (Exception e) {
            log.error("Error updating 2FA setting for user ID: {}", userId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to update 2FA setting: " + e.getMessage()));
        }
    }

    /**
     * Update privacy level setting.
     */
    @PutMapping("/user/{userId}/privacy-level")
    @Operation(summary = "Update privacy level", description = "Update user privacy level setting")
    public ResponseEntity<ApiResponse<UserSettings>> updatePrivacyLevel(
            @PathVariable Long userId,
            @RequestParam UserSettings.PrivacyLevel privacyLevel) {
        log.info("Updating privacy level for user ID: {} to: {}", userId, privacyLevel);
        
        try {
            Optional<UserSettings> settingsOpt = userSettingsRepository.findByUserId(userId);
            if (settingsOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Settings not found for user ID: " + userId));
            }
            
            UserSettings settings = settingsOpt.get();
            settings.setPrivacyLevel(privacyLevel);
            settings.setUpdatedAt(LocalDateTime.now());
            
            UserSettings savedSettings = userSettingsRepository.save(settings);
            
            log.info("Privacy level updated successfully for user ID: {}", userId);
            return ResponseEntity.ok(ApiResponse.success(savedSettings, "Privacy level updated successfully"));
        } catch (Exception e) {
            log.error("Error updating privacy level for user ID: {}", userId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to update privacy level: " + e.getMessage()));
        }
    }

    /**
     * Schedule account deletion.
     */
    @PutMapping("/user/{userId}/schedule-deletion")
    @Operation(summary = "Schedule account deletion", description = "Schedule account for deletion")
    public ResponseEntity<ApiResponse<UserSettings>> scheduleAccountDeletion(
            @PathVariable Long userId,
            @RequestParam LocalDateTime deletionDate) {
        log.info("Scheduling account deletion for user ID: {} on: {}", userId, deletionDate);
        
        try {
            Optional<UserSettings> settingsOpt = userSettingsRepository.findByUserId(userId);
            if (settingsOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Settings not found for user ID: " + userId));
            }
            
            UserSettings settings = settingsOpt.get();
            settings.setAccountDeletionScheduledAt(deletionDate);
            settings.setUpdatedAt(LocalDateTime.now());
            
            UserSettings savedSettings = userSettingsRepository.save(settings);
            
            log.info("Account deletion scheduled successfully for user ID: {}", userId);
            return ResponseEntity.ok(ApiResponse.success(savedSettings, "Account deletion scheduled successfully"));
        } catch (Exception e) {
            log.error("Error scheduling account deletion for user ID: {}", userId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to schedule account deletion: " + e.getMessage()));
        }
    }

    /**
     * Cancel scheduled account deletion.
     */
    @PutMapping("/user/{userId}/cancel-deletion")
    @Operation(summary = "Cancel account deletion", description = "Cancel scheduled account deletion")
    public ResponseEntity<ApiResponse<UserSettings>> cancelAccountDeletion(@PathVariable Long userId) {
        log.info("Canceling account deletion for user ID: {}", userId);
        
        try {
            Optional<UserSettings> settingsOpt = userSettingsRepository.findByUserId(userId);
            if (settingsOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Settings not found for user ID: " + userId));
            }
            
            UserSettings settings = settingsOpt.get();
            settings.setAccountDeletionScheduledAt(null);
            settings.setUpdatedAt(LocalDateTime.now());
            
            UserSettings savedSettings = userSettingsRepository.save(settings);
            
            log.info("Account deletion canceled successfully for user ID: {}", userId);
            return ResponseEntity.ok(ApiResponse.success(savedSettings, "Account deletion canceled successfully"));
        } catch (Exception e) {
            log.error("Error canceling account deletion for user ID: {}", userId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to cancel account deletion: " + e.getMessage()));
        }
    }
}
