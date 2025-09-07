package com.legacykeep.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User Preferences entity for user settings and preferences.
 * 
 * This entity contains user preferences for notifications, privacy,
 * accessibility, and other personal settings.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Entity
@Table(name = "user_preferences", indexes = {
    @Index(name = "idx_user_preferences_user_id", columnList = "user_id"),
    @Index(name = "idx_user_preferences_profile_visibility", columnList = "profile_visibility"),
    @Index(name = "idx_user_preferences_theme", columnList = "theme_preference")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId; // References auth service user ID

    // Notification Preferences
    @Column(name = "email_notifications", nullable = false)
    private boolean emailNotifications = true;

    @Column(name = "push_notifications", nullable = false)
    private boolean pushNotifications = true;

    @Column(name = "sms_notifications", nullable = false)
    private boolean smsNotifications = false;

    @Column(name = "marketing_emails", nullable = false)
    private boolean marketingEmails = false;

    // Privacy Preferences
    @Enumerated(EnumType.STRING)
    @Column(name = "profile_visibility", nullable = false, length = 20)
    private ProfileVisibility profileVisibility = ProfileVisibility.PRIVATE;

    @Enumerated(EnumType.STRING)
    @Column(name = "story_visibility", nullable = false, length = 20)
    private StoryVisibility storyVisibility = StoryVisibility.FAMILY_ONLY;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_visibility", nullable = false, length = 20)
    private MediaVisibility mediaVisibility = MediaVisibility.FAMILY_ONLY;

    // UI Preferences
    @Enumerated(EnumType.STRING)
    @Column(name = "theme_preference", nullable = false, length = 20)
    private ThemePreference themePreference = ThemePreference.LIGHT;

    @Enumerated(EnumType.STRING)
    @Column(name = "font_size", nullable = false, length = 20)
    private FontSize fontSize = FontSize.MEDIUM;

    @Column(name = "high_contrast", nullable = false)
    private boolean highContrast = false;

    @Column(name = "reduced_motion", nullable = false)
    private boolean reducedMotion = false;

    // Media Preferences
    @Column(name = "auto_play_videos", nullable = false)
    private boolean autoPlayVideos = true;

    @Column(name = "data_saver_mode", nullable = false)
    private boolean dataSaverMode = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(nullable = false)
    private Long version = 0L;

    // =============================================================================
    // Enums
    // =============================================================================

    public enum ProfileVisibility {
        PUBLIC,         // Visible to everyone
        PRIVATE,        // Visible only to user
        FRIENDS_ONLY    // Visible only to friends/family
    }

    public enum StoryVisibility {
        PUBLIC,         // Visible to everyone
        FAMILY_ONLY,    // Visible only to family members
        PRIVATE         // Visible only to user
    }

    public enum MediaVisibility {
        PUBLIC,         // Visible to everyone
        FAMILY_ONLY,    // Visible only to family members
        PRIVATE         // Visible only to user
    }

    public enum ThemePreference {
        LIGHT,          // Light theme
        DARK,           // Dark theme
        AUTO            // Follow system preference
    }

    public enum FontSize {
        SMALL,          // Small font size
        MEDIUM,         // Medium font size
        LARGE,          // Large font size
        EXTRA_LARGE     // Extra large font size
    }

    // =============================================================================
    // Constructors
    // =============================================================================

    public UserPreferences() {
        // Default constructor for JPA
    }

    public UserPreferences(Long userId) {
        this.userId = userId;
    }

    // =============================================================================
    // Business Methods
    // =============================================================================

    /**
     * Check if any notifications are enabled.
     * 
     * @return true if at least one notification type is enabled
     */
    public boolean hasAnyNotificationsEnabled() {
        return emailNotifications || pushNotifications || smsNotifications;
    }

    /**
     * Check if the user has accessibility features enabled.
     * 
     * @return true if any accessibility features are enabled
     */
    public boolean hasAccessibilityFeaturesEnabled() {
        return highContrast || reducedMotion || fontSize != FontSize.MEDIUM;
    }

    /**
     * Check if the user prefers data-saving mode.
     * 
     * @return true if data saver mode is enabled
     */
    public boolean isDataSaverEnabled() {
        return dataSaverMode;
    }

    /**
     * Get the effective theme based on user preference.
     * 
     * @param systemTheme the system theme preference
     * @return the effective theme to use
     */
    public ThemePreference getEffectiveTheme(ThemePreference systemTheme) {
        if (themePreference == ThemePreference.AUTO) {
            return systemTheme != null ? systemTheme : ThemePreference.LIGHT;
        }
        return themePreference;
    }

    // =============================================================================
    // Equals and HashCode
    // =============================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPreferences that = (UserPreferences) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
