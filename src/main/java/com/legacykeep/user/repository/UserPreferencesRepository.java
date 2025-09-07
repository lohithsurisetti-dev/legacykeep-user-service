package com.legacykeep.user.repository;

import com.legacykeep.user.entity.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserPreferences entity operations.
 * 
 * Provides data access methods for user preferences management including
 * CRUD operations and preference-based queries.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {

    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================

    /**
     * Find user preferences by user ID.
     * 
     * @param userId the user ID from auth service
     * @return Optional containing the user preferences if found
     */
    Optional<UserPreferences> findByUserId(Long userId);

    /**
     * Check if user preferences exist by user ID.
     * 
     * @param userId the user ID from auth service
     * @return true if preferences exist
     */
    boolean existsByUserId(Long userId);

    /**
     * Delete user preferences by user ID.
     * 
     * @param userId the user ID from auth service
     */
    void deleteByUserId(Long userId);

    // =============================================================================
    // Notification Preferences
    // =============================================================================

    /**
     * Find users with email notifications enabled.
     * 
     * @return list of user IDs with email notifications enabled
     */
    @Query("SELECT up.userId FROM UserPreferences up WHERE up.emailNotifications = true")
    List<Long> findUserIdsWithEmailNotificationsEnabled();

    /**
     * Find users with push notifications enabled.
     * 
     * @return list of user IDs with push notifications enabled
     */
    @Query("SELECT up.userId FROM UserPreferences up WHERE up.pushNotifications = true")
    List<Long> findUserIdsWithPushNotificationsEnabled();

    /**
     * Find users with SMS notifications enabled.
     * 
     * @return list of user IDs with SMS notifications enabled
     */
    @Query("SELECT up.userId FROM UserPreferences up WHERE up.smsNotifications = true")
    List<Long> findUserIdsWithSmsNotificationsEnabled();

    /**
     * Find users with marketing emails enabled.
     * 
     * @return list of user IDs with marketing emails enabled
     */
    @Query("SELECT up.userId FROM UserPreferences up WHERE up.marketingEmails = true")
    List<Long> findUserIdsWithMarketingEmailsEnabled();

    // =============================================================================
    // Privacy Preferences
    // =============================================================================

    /**
     * Find users with public profile visibility.
     * 
     * @return list of user preferences with public profile visibility
     */
    List<UserPreferences> findByProfileVisibility(UserPreferences.ProfileVisibility visibility);

    /**
     * Find users with specific story visibility.
     * 
     * @param visibility the story visibility level
     * @return list of user preferences with the specified story visibility
     */
    List<UserPreferences> findByStoryVisibility(UserPreferences.StoryVisibility visibility);

    /**
     * Find users with specific media visibility.
     * 
     * @param visibility the media visibility level
     * @return list of user preferences with the specified media visibility
     */
    List<UserPreferences> findByMediaVisibility(UserPreferences.MediaVisibility visibility);

    // =============================================================================
    // UI Preferences
    // =============================================================================

    /**
     * Find users with specific theme preference.
     * 
     * @param theme the theme preference
     * @return list of user preferences with the specified theme
     */
    List<UserPreferences> findByThemePreference(UserPreferences.ThemePreference theme);

    /**
     * Find users with specific font size preference.
     * 
     * @param fontSize the font size preference
     * @return list of user preferences with the specified font size
     */
    List<UserPreferences> findByFontSize(UserPreferences.FontSize fontSize);

    /**
     * Find users with high contrast enabled.
     * 
     * @return list of user preferences with high contrast enabled
     */
    List<UserPreferences> findByHighContrastTrue();

    /**
     * Find users with reduced motion enabled.
     * 
     * @return list of user preferences with reduced motion enabled
     */
    List<UserPreferences> findByReducedMotionTrue();

    // =============================================================================
    // Media Preferences
    // =============================================================================

    /**
     * Find users with auto-play videos enabled.
     * 
     * @return list of user preferences with auto-play videos enabled
     */
    List<UserPreferences> findByAutoPlayVideosTrue();

    /**
     * Find users with data saver mode enabled.
     * 
     * @return list of user preferences with data saver mode enabled
     */
    List<UserPreferences> findByDataSaverModeTrue();

    // =============================================================================
    // Custom Queries
    // =============================================================================

    /**
     * Find users with any notifications enabled.
     * 
     * @return list of user IDs with at least one notification type enabled
     */
    @Query("SELECT up.userId FROM UserPreferences up WHERE " +
           "up.emailNotifications = true OR " +
           "up.pushNotifications = true OR " +
           "up.smsNotifications = true")
    List<Long> findUserIdsWithAnyNotificationsEnabled();

    /**
     * Find users with accessibility features enabled.
     * 
     * @return list of user preferences with accessibility features enabled
     */
    @Query("SELECT up FROM UserPreferences up WHERE " +
           "up.highContrast = true OR " +
           "up.reducedMotion = true OR " +
           "up.fontSize != 'MEDIUM'")
    List<UserPreferences> findUsersWithAccessibilityFeatures();

    /**
     * Find users with minimal privacy settings.
     * 
     * @return list of user preferences with minimal privacy settings
     */
    @Query("SELECT up FROM UserPreferences up WHERE " +
           "up.profileVisibility = 'PUBLIC' AND " +
           "up.storyVisibility = 'PUBLIC' AND " +
           "up.mediaVisibility = 'PUBLIC'")
    List<UserPreferences> findUsersWithMinimalPrivacy();

    /**
     * Find users with maximum privacy settings.
     * 
     * @return list of user preferences with maximum privacy settings
     */
    @Query("SELECT up FROM UserPreferences up WHERE " +
           "up.profileVisibility = 'PRIVATE' AND " +
           "up.storyVisibility = 'PRIVATE' AND " +
           "up.mediaVisibility = 'PRIVATE'")
    List<UserPreferences> findUsersWithMaximumPrivacy();

    // =============================================================================
    // Analytics Queries
    // =============================================================================

    /**
     * Count users with email notifications enabled.
     * 
     * @return count of users with email notifications enabled
     */
    @Query("SELECT COUNT(up) FROM UserPreferences up WHERE up.emailNotifications = true")
    long countUsersWithEmailNotifications();

    /**
     * Count users with push notifications enabled.
     * 
     * @return count of users with push notifications enabled
     */
    @Query("SELECT COUNT(up) FROM UserPreferences up WHERE up.pushNotifications = true")
    long countUsersWithPushNotifications();

    /**
     * Count users with SMS notifications enabled.
     * 
     * @return count of users with SMS notifications enabled
     */
    @Query("SELECT COUNT(up) FROM UserPreferences up WHERE up.smsNotifications = true")
    long countUsersWithSmsNotifications();

    /**
     * Count users with marketing emails enabled.
     * 
     * @return count of users with marketing emails enabled
     */
    @Query("SELECT COUNT(up) FROM UserPreferences up WHERE up.marketingEmails = true")
    long countUsersWithMarketingEmails();

    /**
     * Count users by theme preference.
     * 
     * @param theme the theme preference
     * @return count of users with the specified theme
     */
    long countByThemePreference(UserPreferences.ThemePreference theme);

    /**
     * Count users by profile visibility.
     * 
     * @param visibility the profile visibility
     * @return count of users with the specified profile visibility
     */
    long countByProfileVisibility(UserPreferences.ProfileVisibility visibility);

    /**
     * Count users with accessibility features enabled.
     * 
     * @return count of users with accessibility features enabled
     */
    @Query("SELECT COUNT(up) FROM UserPreferences up WHERE " +
           "up.highContrast = true OR " +
           "up.reducedMotion = true OR " +
           "up.fontSize != 'MEDIUM'")
    long countUsersWithAccessibilityFeatures();

    /**
     * Count users with data saver mode enabled.
     * 
     * @return count of users with data saver mode enabled
     */
    @Query("SELECT COUNT(up) FROM UserPreferences up WHERE up.dataSaverMode = true")
    long countUsersWithDataSaverMode();
}
