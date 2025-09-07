package com.legacykeep.user.repository;

import com.legacykeep.user.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserSettings entity operations.
 * 
 * Provides data access methods for user settings management including
 * CRUD operations and security-related queries.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================

    /**
     * Find user settings by user ID.
     * 
     * @param userId the user ID from auth service
     * @return Optional containing the user settings if found
     */
    Optional<UserSettings> findByUserId(Long userId);

    /**
     * Check if user settings exist by user ID.
     * 
     * @param userId the user ID from auth service
     * @return true if settings exist
     */
    boolean existsByUserId(Long userId);

    /**
     * Delete user settings by user ID.
     * 
     * @param userId the user ID from auth service
     */
    void deleteByUserId(Long userId);

    // =============================================================================
    // Security Settings
    // =============================================================================

    /**
     * Find users with two-factor authentication enabled.
     * 
     * @return list of user IDs with 2FA enabled
     */
    @Query("SELECT us.userId FROM UserSettings us WHERE us.twoFactorEnabled = true")
    List<Long> findUserIdsWithTwoFactorEnabled();

    /**
     * Find users with login notifications enabled.
     * 
     * @return list of user IDs with login notifications enabled
     */
    @Query("SELECT us.userId FROM UserSettings us WHERE us.loginNotifications = true")
    List<Long> findUserIdsWithLoginNotificationsEnabled();

    /**
     * Find users with device management enabled.
     * 
     * @return list of user IDs with device management enabled
     */
    @Query("SELECT us.userId FROM UserSettings us WHERE us.deviceManagement = true")
    List<Long> findUserIdsWithDeviceManagementEnabled();

    // =============================================================================
    // Data Management
    // =============================================================================

    /**
     * Find users with data export enabled.
     * 
     * @return list of user IDs with data export enabled
     */
    @Query("SELECT us.userId FROM UserSettings us WHERE us.dataExportEnabled = true")
    List<Long> findUserIdsWithDataExportEnabled();

    /**
     * Find users with scheduled account deletion.
     * 
     * @return list of user settings with scheduled account deletion
     */
    List<UserSettings> findByAccountDeletionScheduledAtIsNotNull();

    /**
     * Find users with account deletion scheduled before a specific date.
     * 
     * @param date the date to filter by
     * @return list of user settings with deletion scheduled before the date
     */
    List<UserSettings> findByAccountDeletionScheduledAtBefore(LocalDateTime date);

    /**
     * Find users with account deletion scheduled after a specific date.
     * 
     * @param date the date to filter by
     * @return list of user settings with deletion scheduled after the date
     */
    List<UserSettings> findByAccountDeletionScheduledAtAfter(LocalDateTime date);

    // =============================================================================
    // Privacy Settings
    // =============================================================================

    /**
     * Find users with specific privacy level.
     * 
     * @param privacyLevel the privacy level
     * @return list of user settings with the specified privacy level
     */
    List<UserSettings> findByPrivacyLevel(UserSettings.PrivacyLevel privacyLevel);

    /**
     * Find users with minimal privacy settings.
     * 
     * @return list of user settings with minimal privacy
     */
    List<UserSettings> findByPrivacyLevel(UserSettings.PrivacyLevel.MINIMAL);

    /**
     * Find users with maximum privacy settings.
     * 
     * @return list of user settings with maximum privacy
     */
    List<UserSettings> findByPrivacyLevel(UserSettings.PrivacyLevel.MAXIMUM);

    // =============================================================================
    // Activity Tracking
    // =============================================================================

    /**
     * Find users who haven't changed their password recently.
     * 
     * @param date the date to filter by
     * @return list of user settings with last password change before the date
     */
    List<UserSettings> findByLastPasswordChangeAtBefore(LocalDateTime date);

    /**
     * Find users who haven't updated their profile recently.
     * 
     * @param date the date to filter by
     * @return list of user settings with last profile update before the date
     */
    List<UserSettings> findByLastProfileUpdateAtBefore(LocalDateTime date);

    /**
     * Find users with no password change recorded.
     * 
     * @return list of user settings with no password change timestamp
     */
    List<UserSettings> findByLastPasswordChangeAtIsNull();

    /**
     * Find users with no profile update recorded.
     * 
     * @return list of user settings with no profile update timestamp
     */
    List<UserSettings> findByLastProfileUpdateAtIsNull();

    // =============================================================================
    // Custom Queries
    // =============================================================================

    /**
     * Find users with high security settings enabled.
     * 
     * @return list of user settings with high security enabled
     */
    @Query("SELECT us FROM UserSettings us WHERE " +
           "us.twoFactorEnabled = true AND " +
           "us.loginNotifications = true AND " +
           "us.deviceManagement = true")
    List<UserSettings> findUsersWithHighSecurityEnabled();

    /**
     * Find users with low security settings.
     * 
     * @return list of user settings with low security
     */
    @Query("SELECT us FROM UserSettings us WHERE " +
           "us.twoFactorEnabled = false AND " +
           "us.loginNotifications = false AND " +
           "us.deviceManagement = false")
    List<UserSettings> findUsersWithLowSecurity();

    /**
     * Find users with specific data retention period.
     * 
     * @param retentionDays the data retention period in days
     * @return list of user settings with the specified retention period
     */
    List<UserSettings> findByDataRetentionDays(Integer retentionDays);

    /**
     * Find users with data retention less than specified days.
     * 
     * @param retentionDays the data retention period in days
     * @return list of user settings with retention less than specified
     */
    List<UserSettings> findByDataRetentionDaysLessThan(Integer retentionDays);

    /**
     * Find users with data retention greater than specified days.
     * 
     * @param retentionDays the data retention period in days
     * @return list of user settings with retention greater than specified
     */
    List<UserSettings> findByDataRetentionDaysGreaterThan(Integer retentionDays);

    // =============================================================================
    // Analytics Queries
    // =============================================================================

    /**
     * Count users with two-factor authentication enabled.
     * 
     * @return count of users with 2FA enabled
     */
    @Query("SELECT COUNT(us) FROM UserSettings us WHERE us.twoFactorEnabled = true")
    long countUsersWithTwoFactorEnabled();

    /**
     * Count users with login notifications enabled.
     * 
     * @return count of users with login notifications enabled
     */
    @Query("SELECT COUNT(us) FROM UserSettings us WHERE us.loginNotifications = true")
    long countUsersWithLoginNotifications();

    /**
     * Count users with device management enabled.
     * 
     * @return count of users with device management enabled
     */
    @Query("SELECT COUNT(us) FROM UserSettings us WHERE us.deviceManagement = true")
    long countUsersWithDeviceManagement();

    /**
     * Count users with data export enabled.
     * 
     * @return count of users with data export enabled
     */
    @Query("SELECT COUNT(us) FROM UserSettings us WHERE us.dataExportEnabled = true")
    long countUsersWithDataExportEnabled();

    /**
     * Count users with scheduled account deletion.
     * 
     * @return count of users with scheduled account deletion
     */
    @Query("SELECT COUNT(us) FROM UserSettings us WHERE us.accountDeletionScheduledAt IS NOT NULL")
    long countUsersWithScheduledDeletion();

    /**
     * Count users by privacy level.
     * 
     * @param privacyLevel the privacy level
     * @return count of users with the specified privacy level
     */
    long countByPrivacyLevel(UserSettings.PrivacyLevel privacyLevel);

    /**
     * Count users with high security settings.
     * 
     * @return count of users with high security enabled
     */
    @Query("SELECT COUNT(us) FROM UserSettings us WHERE " +
           "us.twoFactorEnabled = true AND " +
           "us.loginNotifications = true AND " +
           "us.deviceManagement = true")
    long countUsersWithHighSecurity();

    /**
     * Count users with low security settings.
     * 
     * @return count of users with low security
     */
    @Query("SELECT COUNT(us) FROM UserSettings us WHERE " +
           "us.twoFactorEnabled = false AND " +
           "us.loginNotifications = false AND " +
           "us.deviceManagement = false")
    long countUsersWithLowSecurity();

    /**
     * Get average data retention period.
     * 
     * @return average data retention period in days
     */
    @Query("SELECT AVG(us.dataRetentionDays) FROM UserSettings us")
    Double getAverageDataRetentionDays();

    /**
     * Count users who haven't changed their password recently.
     * 
     * @param date the date to filter by
     * @return count of users with last password change before the date
     */
    @Query("SELECT COUNT(us) FROM UserSettings us WHERE us.lastPasswordChangeAt < :date")
    long countUsersWithOldPassword(@Param("date") LocalDateTime date);

    /**
     * Count users who haven't updated their profile recently.
     * 
     * @param date the date to filter by
     * @return count of users with last profile update before the date
     */
    @Query("SELECT COUNT(us) FROM UserSettings us WHERE us.lastProfileUpdateAt < :date")
    long countUsersWithOldProfile(@Param("date") LocalDateTime date);
}
