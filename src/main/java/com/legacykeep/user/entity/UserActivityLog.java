package com.legacykeep.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User Activity Log entity for tracking user actions and analytics.
 * 
 * This entity logs user activities for analytics, security monitoring,
 * and user behavior analysis.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Entity
@Table(name = "user_activity_log", indexes = {
    @Index(name = "idx_user_activity_log_user_id", columnList = "user_id"),
    @Index(name = "idx_user_activity_log_type", columnList = "activity_type"),
    @Index(name = "idx_user_activity_log_created_at", columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"activityDescription", "userAgent", "deviceInfo", "locationInfo", "metadata"})
public class UserActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // References auth service user ID

    @NotNull(message = "Activity type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false, length = 50)
    private ActivityType activityType;

    @Size(max = 1000, message = "Activity description must not exceed 1000 characters")
    @Column(name = "activity_description", columnDefinition = "TEXT")
    private String activityDescription;

    @Size(max = 45, message = "IP address must not exceed 45 characters")
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "device_info", columnDefinition = "TEXT")
    private String deviceInfo;

    @Column(name = "location_info", columnDefinition = "TEXT")
    private String locationInfo;

    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadata; // JSON string for additional data

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // =============================================================================
    // Enums
    // =============================================================================

    public enum ActivityType {
        // Profile Activities
        PROFILE_CREATED,
        PROFILE_UPDATED,
        PROFILE_PICTURE_UPDATED,
        PROFILE_DELETED,
        
        // Preference Activities
        PREFERENCES_UPDATED,
        NOTIFICATION_SETTINGS_CHANGED,
        PRIVACY_SETTINGS_CHANGED,
        THEME_CHANGED,
        
        // Interest Activities
        INTEREST_ADDED,
        INTEREST_UPDATED,
        INTEREST_DELETED,
        
        // Account Activities
        ACCOUNT_LOGIN,
        ACCOUNT_LOGOUT,
        PASSWORD_CHANGED,
        TWO_FACTOR_ENABLED,
        TWO_FACTOR_DISABLED,
        
        // Data Activities
        DATA_EXPORT_REQUESTED,
        DATA_DELETION_REQUESTED,
        ACCOUNT_DELETION_SCHEDULED,
        ACCOUNT_DELETION_CANCELLED,
        
        // Security Activities
        SUSPICIOUS_ACTIVITY_DETECTED,
        SECURITY_SETTINGS_CHANGED,
        DEVICE_ADDED,
        DEVICE_REMOVED,
        
        // Family Activities (for future integration)
        FAMILY_INVITATION_SENT,
        FAMILY_INVITATION_ACCEPTED,
        FAMILY_INVITATION_DECLINED,
        FAMILY_MEMBER_ADDED,
        FAMILY_MEMBER_REMOVED,
        
        // Story Activities (for future integration)
        STORY_CREATED,
        STORY_UPDATED,
        STORY_DELETED,
        STORY_SHARED,
        
        // Media Activities (for future integration)
        MEDIA_UPLOADED,
        MEDIA_DELETED,
        MEDIA_SHARED,
        
        // Other Activities
        SETTINGS_VIEWED,
        HELP_ACCESSED,
        SUPPORT_CONTACTED,
        FEEDBACK_SUBMITTED,
        OTHER
    }

    // =============================================================================
    // Constructors
    // =============================================================================

    public UserActivityLog() {
        // Default constructor for JPA
    }

    public UserActivityLog(Long userId, ActivityType activityType) {
        this.userId = userId;
        this.activityType = activityType;
    }

    public UserActivityLog(Long userId, ActivityType activityType, String activityDescription) {
        this.userId = userId;
        this.activityType = activityType;
        this.activityDescription = activityDescription;
    }

    // =============================================================================
    // Business Methods
    // =============================================================================

    /**
     * Check if the activity is a security-related activity.
     * 
     * @return true if the activity is security-related
     */
    public boolean isSecurityActivity() {
        return activityType == ActivityType.ACCOUNT_LOGIN ||
               activityType == ActivityType.ACCOUNT_LOGOUT ||
               activityType == ActivityType.PASSWORD_CHANGED ||
               activityType == ActivityType.TWO_FACTOR_ENABLED ||
               activityType == ActivityType.TWO_FACTOR_DISABLED ||
               activityType == ActivityType.SUSPICIOUS_ACTIVITY_DETECTED ||
               activityType == ActivityType.SECURITY_SETTINGS_CHANGED ||
               activityType == ActivityType.DEVICE_ADDED ||
               activityType == ActivityType.DEVICE_REMOVED;
    }

    /**
     * Check if the activity is a profile-related activity.
     * 
     * @return true if the activity is profile-related
     */
    public boolean isProfileActivity() {
        return activityType == ActivityType.PROFILE_CREATED ||
               activityType == ActivityType.PROFILE_UPDATED ||
               activityType == ActivityType.PROFILE_PICTURE_UPDATED ||
               activityType == ActivityType.PROFILE_DELETED;
    }

    /**
     * Check if the activity is a preference-related activity.
     * 
     * @return true if the activity is preference-related
     */
    public boolean isPreferenceActivity() {
        return activityType == ActivityType.PREFERENCES_UPDATED ||
               activityType == ActivityType.NOTIFICATION_SETTINGS_CHANGED ||
               activityType == ActivityType.PRIVACY_SETTINGS_CHANGED ||
               activityType == ActivityType.THEME_CHANGED;
    }

    /**
     * Check if the activity is a data-related activity.
     * 
     * @return true if the activity is data-related
     */
    public boolean isDataActivity() {
        return activityType == ActivityType.DATA_EXPORT_REQUESTED ||
               activityType == ActivityType.DATA_DELETION_REQUESTED ||
               activityType == ActivityType.ACCOUNT_DELETION_SCHEDULED ||
               activityType == ActivityType.ACCOUNT_DELETION_CANCELLED;
    }

    /**
     * Get a display-friendly version of the activity type.
     * 
     * @return the formatted activity type
     */
    public String getDisplayType() {
        return activityType.name().toLowerCase().replace("_", " ");
    }

    /**
     * Get the activity age in minutes.
     * 
     * @return the age in minutes
     */
    public long getAgeInMinutes() {
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toMinutes();
    }

    /**
     * Get the activity age in hours.
     * 
     * @return the age in hours
     */
    public long getAgeInHours() {
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toHours();
    }

    /**
     * Get the activity age in days.
     * 
     * @return the age in days
     */
    public long getAgeInDays() {
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toDays();
    }

    // =============================================================================
    // Equals and HashCode
    // =============================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActivityLog that = (UserActivityLog) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
