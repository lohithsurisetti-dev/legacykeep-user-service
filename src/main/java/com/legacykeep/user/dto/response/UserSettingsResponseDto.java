package com.legacykeep.user.dto.response;

import com.legacykeep.user.entity.UserSettings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user settings response data.
 * 
 * Contains user settings information returned by the API
 * including security settings, data management, and privacy settings.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsResponseDto {

    private Long id;
    private Long userId;

    // Security Settings
    private boolean twoFactorEnabled;
    private boolean loginNotifications;
    private boolean deviceManagement;

    // Data Management
    private boolean dataExportEnabled;
    private LocalDateTime accountDeletionScheduledAt;

    // Activity Tracking
    private LocalDateTime lastPasswordChangeAt;
    private LocalDateTime lastProfileUpdateAt;

    // Privacy Settings
    private UserSettings.PrivacyLevel privacyLevel;
    private Integer dataRetentionDays;
    private Double dataRetentionYears;

    // Computed Properties
    private boolean isAccountDeletionScheduled;
    private boolean hasHighSecurityEnabled;
    private boolean hasMinimalPrivacy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}
