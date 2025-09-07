package com.legacykeep.user.dto.request;

import com.legacykeep.user.entity.UserPreferences;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for user preferences update requests.
 * 
 * Contains validation rules for user preferences including
 * notification settings, privacy preferences, and UI settings.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
public class UserPreferencesRequestDto {

    // Notification Preferences
    private Boolean emailNotifications;
    private Boolean pushNotifications;
    private Boolean smsNotifications;
    private Boolean marketingEmails;

    // Privacy Preferences
    @NotNull(message = "Profile visibility is required")
    private UserPreferences.ProfileVisibility profileVisibility;

    @NotNull(message = "Story visibility is required")
    private UserPreferences.StoryVisibility storyVisibility;

    @NotNull(message = "Media visibility is required")
    private UserPreferences.MediaVisibility mediaVisibility;

    // UI Preferences
    @NotNull(message = "Theme preference is required")
    private UserPreferences.ThemePreference themePreference;

    @NotNull(message = "Font size is required")
    private UserPreferences.FontSize fontSize;

    private Boolean highContrast;
    private Boolean reducedMotion;

    // Media Preferences
    private Boolean autoPlayVideos;
    private Boolean dataSaverMode;
}
