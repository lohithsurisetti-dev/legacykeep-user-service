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
 * User Settings entity for account and security settings.
 * 
 * This entity contains user account settings, security preferences,
 * and administrative settings.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Entity
@Table(name = "user_settings", indexes = {
    @Index(name = "idx_user_settings_user_id", columnList = "user_id"),
    @Index(name = "idx_user_settings_two_factor", columnList = "two_factor_enabled"),
    @Index(name = "idx_user_settings_privacy_level", columnList = "privacy_level")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId; // References auth service user ID

    // Security Settings
    @Column(name = "two_factor_enabled", nullable = false)
    private boolean twoFactorEnabled = false;

    @Column(name = "login_notifications", nullable = false)
    private boolean loginNotifications = true;

    @Column(name = "device_management", nullable = false)
    private boolean deviceManagement = true;

    // Data Management
    @Column(name = "data_export_enabled", nullable = false)
    private boolean dataExportEnabled = true;

    @Column(name = "account_deletion_scheduled_at")
    private LocalDateTime accountDeletionScheduledAt;

    // Activity Tracking
    @Column(name = "last_password_change_at")
    private LocalDateTime lastPasswordChangeAt;

    @Column(name = "last_profile_update_at")
    private LocalDateTime lastProfileUpdateAt;

    // Privacy Settings
    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_level", nullable = false, length = 20)
    private PrivacyLevel privacyLevel = PrivacyLevel.STANDARD;

    @Column(name = "data_retention_days", nullable = false)
    private Integer dataRetentionDays = 2555; // 7 years default

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

    public enum PrivacyLevel {
        MINIMAL,        // Minimal data collection and retention
        STANDARD,       // Standard data collection and retention
        MAXIMUM         // Maximum data collection and retention
    }

    // =============================================================================
    // Constructors
    // =============================================================================

    public UserSettings() {
        // Default constructor for JPA
    }

    public UserSettings(Long userId) {
        this.userId = userId;
    }

    // =============================================================================
    // Business Methods
    // =============================================================================

    /**
     * Check if the account is scheduled for deletion.
     * 
     * @return true if account deletion is scheduled
     */
    public boolean isAccountDeletionScheduled() {
        return accountDeletionScheduledAt != null;
    }

    /**
     * Schedule account deletion.
     * 
     * @param deletionDate the date when the account should be deleted
     */
    public void scheduleAccountDeletion(LocalDateTime deletionDate) {
        this.accountDeletionScheduledAt = deletionDate;
    }

    /**
     * Cancel scheduled account deletion.
     */
    public void cancelAccountDeletion() {
        this.accountDeletionScheduledAt = null;
    }

    /**
     * Update the last password change timestamp.
     */
    public void updatePasswordChangeTimestamp() {
        this.lastPasswordChangeAt = LocalDateTime.now();
    }

    /**
     * Update the last profile update timestamp.
     */
    public void updateProfileUpdateTimestamp() {
        this.lastProfileUpdateAt = LocalDateTime.now();
    }

    /**
     * Check if the user has high security settings enabled.
     * 
     * @return true if high security features are enabled
     */
    public boolean hasHighSecurityEnabled() {
        return twoFactorEnabled && loginNotifications && deviceManagement;
    }

    /**
     * Check if the user has minimal privacy settings.
     * 
     * @return true if privacy level is minimal
     */
    public boolean hasMinimalPrivacy() {
        return privacyLevel == PrivacyLevel.MINIMAL;
    }

    /**
     * Get the data retention period in years.
     * 
     * @return the data retention period in years
     */
    public double getDataRetentionYears() {
        return dataRetentionDays / 365.0;
    }

    /**
     * Set the data retention period in years.
     * 
     * @param years the data retention period in years
     */
    public void setDataRetentionYears(double years) {
        this.dataRetentionDays = (int) Math.round(years * 365);
    }

    // =============================================================================
    // Equals and HashCode
    // =============================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSettings that = (UserSettings) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
