package com.legacykeep.user.dto.response;

import com.legacykeep.user.entity.UserPreferences;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user preferences response data.
 * 
 * Contains user preferences information returned by the API
 * including notification settings, privacy preferences, and UI settings.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencesResponseDto {

    private Long id;
    private Long userId;

    // Notification Preferences
    private boolean emailNotifications;
    private boolean pushNotifications;
    private boolean smsNotifications;
    private boolean marketingEmails;

    // Privacy Preferences
    private UserPreferences.ProfileVisibility profileVisibility;
    private UserPreferences.StoryVisibility storyVisibility;
    private UserPreferences.MediaVisibility mediaVisibility;

    // UI Preferences
    private UserPreferences.ThemePreference themePreference;
    private UserPreferences.FontSize fontSize;
    private boolean highContrast;
    private boolean reducedMotion;

    // Media Preferences
    private boolean autoPlayVideos;
    private boolean dataSaverMode;

    // Computed Properties
    private boolean hasAnyNotificationsEnabled;
    private boolean hasAccessibilityFeaturesEnabled;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}
