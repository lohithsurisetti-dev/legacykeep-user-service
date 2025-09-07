package com.legacykeep.user.dto.request;

import com.legacykeep.user.entity.UserSettings;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for user settings update requests.
 * 
 * Contains validation rules for user settings including
 * security settings, data management, and privacy settings.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
public class UserSettingsRequestDto {

    // Security Settings
    private Boolean twoFactorEnabled;
    private Boolean loginNotifications;
    private Boolean deviceManagement;

    // Data Management
    private Boolean dataExportEnabled;

    // Privacy Settings
    @NotNull(message = "Privacy level is required")
    private UserSettings.PrivacyLevel privacyLevel;

    @Min(value = 30, message = "Data retention period must be at least 30 days")
    @Max(value = 3650, message = "Data retention period must not exceed 10 years")
    private Integer dataRetentionDays;
}
