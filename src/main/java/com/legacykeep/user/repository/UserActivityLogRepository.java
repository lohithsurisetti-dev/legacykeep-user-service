package com.legacykeep.user.repository;

import com.legacykeep.user.entity.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for UserActivityLog entity operations.
 * 
 * Provides data access methods for user activity logging including
 * CRUD operations and analytics queries.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {

    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================

    /**
     * Find user activity logs by user ID.
     * 
     * @param userId the user ID from auth service
     * @return list of activity logs for the user
     */
    List<UserActivityLog> findByUserId(Long userId);

    /**
     * Find user activity logs by user ID, ordered by creation date (newest first).
     * 
     * @param userId the user ID from auth service
     * @return list of activity logs for the user, newest first
     */
    List<UserActivityLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Find user activity logs by user ID and activity type.
     * 
     * @param userId the user ID from auth service
     * @param activityType the activity type
     * @return list of activity logs for the user with the specified type
     */
    List<UserActivityLog> findByUserIdAndActivityType(Long userId, UserActivityLog.ActivityType activityType);

    /**
     * Delete all user activity logs by user ID.
     * 
     * @param userId the user ID from auth service
     */
    void deleteByUserId(Long userId);

    // =============================================================================
    // Activity Type Queries
    // =============================================================================

    /**
     * Find activity logs by activity type.
     * 
     * @param activityType the activity type
     * @return list of activity logs with the specified type
     */
    List<UserActivityLog> findByActivityType(UserActivityLog.ActivityType activityType);

    /**
     * Find activity logs by activity type, ordered by creation date (newest first).
     * 
     * @param activityType the activity type
     * @return list of activity logs with the specified type, newest first
     */
    List<UserActivityLog> findByActivityTypeOrderByCreatedAtDesc(UserActivityLog.ActivityType activityType);

    // =============================================================================
    // Date-based Queries
    // =============================================================================

    /**
     * Find activity logs created after a specific date.
     * 
     * @param date the date to filter by
     * @return list of activity logs created after the date
     */
    List<UserActivityLog> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find activity logs created before a specific date.
     * 
     * @param date the date to filter by
     * @return list of activity logs created before the date
     */
    List<UserActivityLog> findByCreatedAtBefore(LocalDateTime date);

    /**
     * Find activity logs created between two dates.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return list of activity logs created between the dates
     */
    List<UserActivityLog> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find user activity logs created after a specific date.
     * 
     * @param userId the user ID from auth service
     * @param date the date to filter by
     * @return list of activity logs for the user created after the date
     */
    List<UserActivityLog> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime date);

    /**
     * Find user activity logs created before a specific date.
     * 
     * @param userId the user ID from auth service
     * @param date the date to filter by
     * @return list of activity logs for the user created before the date
     */
    List<UserActivityLog> findByUserIdAndCreatedAtBefore(Long userId, LocalDateTime date);

    /**
     * Find user activity logs created between two dates.
     * 
     * @param userId the user ID from auth service
     * @param startDate the start date
     * @param endDate the end date
     * @return list of activity logs for the user created between the dates
     */
    List<UserActivityLog> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    // =============================================================================
    // IP Address Queries
    // =============================================================================

    /**
     * Find activity logs by IP address.
     * 
     * @param ipAddress the IP address
     * @return list of activity logs from the specified IP address
     */
    List<UserActivityLog> findByIpAddress(String ipAddress);

    /**
     * Find user activity logs by IP address.
     * 
     * @param userId the user ID from auth service
     * @param ipAddress the IP address
     * @return list of activity logs for the user from the specified IP address
     */
    List<UserActivityLog> findByUserIdAndIpAddress(Long userId, String ipAddress);

    // =============================================================================
    // Custom Queries
    // =============================================================================

    /**
     * Find recent activity logs for a user.
     * 
     * @param userId the user ID from auth service
     * @param limit the maximum number of results
     * @return list of recent activity logs for the user
     */
    @Query("SELECT ual FROM UserActivityLog ual WHERE ual.userId = :userId ORDER BY ual.createdAt DESC")
    List<UserActivityLog> findRecentActivityLogs(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * Find security-related activity logs for a user.
     * 
     * @param userId the user ID from auth service
     * @return list of security-related activity logs for the user
     */
    @Query("SELECT ual FROM UserActivityLog ual WHERE ual.userId = :userId AND " +
           "(ual.activityType = 'ACCOUNT_LOGIN' OR " +
           "ual.activityType = 'ACCOUNT_LOGOUT' OR " +
           "ual.activityType = 'PASSWORD_CHANGED' OR " +
           "ual.activityType = 'TWO_FACTOR_ENABLED' OR " +
           "ual.activityType = 'TWO_FACTOR_DISABLED' OR " +
           "ual.activityType = 'SUSPICIOUS_ACTIVITY_DETECTED' OR " +
           "ual.activityType = 'SECURITY_SETTINGS_CHANGED' OR " +
           "ual.activityType = 'DEVICE_ADDED' OR " +
           "ual.activityType = 'DEVICE_REMOVED') " +
           "ORDER BY ual.createdAt DESC")
    List<UserActivityLog> findSecurityActivityLogs(@Param("userId") Long userId);

    /**
     * Find profile-related activity logs for a user.
     * 
     * @param userId the user ID from auth service
     * @return list of profile-related activity logs for the user
     */
    @Query("SELECT ual FROM UserActivityLog ual WHERE ual.userId = :userId AND " +
           "(ual.activityType = 'PROFILE_CREATED' OR " +
           "ual.activityType = 'PROFILE_UPDATED' OR " +
           "ual.activityType = 'PROFILE_PICTURE_UPDATED' OR " +
           "ual.activityType = 'PROFILE_DELETED') " +
           "ORDER BY ual.createdAt DESC")
    List<UserActivityLog> findProfileActivityLogs(@Param("userId") Long userId);

    /**
     * Find preference-related activity logs for a user.
     * 
     * @param userId the user ID from auth service
     * @return list of preference-related activity logs for the user
     */
    @Query("SELECT ual FROM UserActivityLog ual WHERE ual.userId = :userId AND " +
           "(ual.activityType = 'PREFERENCES_UPDATED' OR " +
           "ual.activityType = 'NOTIFICATION_SETTINGS_CHANGED' OR " +
           "ual.activityType = 'PRIVACY_SETTINGS_CHANGED' OR " +
           "ual.activityType = 'THEME_CHANGED') " +
           "ORDER BY ual.createdAt DESC")
    List<UserActivityLog> findPreferenceActivityLogs(@Param("userId") Long userId);

    /**
     * Find data-related activity logs for a user.
     * 
     * @param userId the user ID from auth service
     * @return list of data-related activity logs for the user
     */
    @Query("SELECT ual FROM UserActivityLog ual WHERE ual.userId = :userId AND " +
           "(ual.activityType = 'DATA_EXPORT_REQUESTED' OR " +
           "ual.activityType = 'DATA_DELETION_REQUESTED' OR " +
           "ual.activityType = 'ACCOUNT_DELETION_SCHEDULED' OR " +
           "ual.activityType = 'ACCOUNT_DELETION_CANCELLED') " +
           "ORDER BY ual.createdAt DESC")
    List<UserActivityLog> findDataActivityLogs(@Param("userId") Long userId);

    /**
     * Find activity logs by description (case-insensitive search).
     * 
     * @param description the description to search for
     * @return list of activity logs with matching descriptions
     */
    @Query("SELECT ual FROM UserActivityLog ual WHERE " +
           "ual.activityDescription ILIKE %:description% " +
           "ORDER BY ual.createdAt DESC")
    List<UserActivityLog> findByDescriptionContaining(@Param("description") String description);

    // =============================================================================
    // Analytics Queries
    // =============================================================================

    /**
     * Count total activity logs.
     * 
     * @return count of all activity logs
     */
    @Query("SELECT COUNT(ual) FROM UserActivityLog ual")
    long countTotalActivityLogs();

    /**
     * Count activity logs by type.
     * 
     * @param activityType the activity type
     * @return count of activity logs with the specified type
     */
    long countByActivityType(UserActivityLog.ActivityType activityType);

    /**
     * Count activity logs for a user.
     * 
     * @param userId the user ID from auth service
     * @return count of activity logs for the user
     */
    long countByUserId(Long userId);

    /**
     * Count activity logs created in date range.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return count of activity logs created in the date range
     */
    @Query("SELECT COUNT(ual) FROM UserActivityLog ual WHERE ual.createdAt BETWEEN :startDate AND :endDate")
    long countActivityLogsCreatedBetween(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);

    /**
     * Count activity logs by type in date range.
     * 
     * @param activityType the activity type
     * @param startDate the start date
     * @param endDate the end date
     * @return count of activity logs with the specified type in the date range
     */
    @Query("SELECT COUNT(ual) FROM UserActivityLog ual WHERE " +
           "ual.activityType = :activityType AND " +
           "ual.createdAt BETWEEN :startDate AND :endDate")
    long countActivityLogsByTypeAndDateRange(@Param("activityType") UserActivityLog.ActivityType activityType,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    /**
     * Count unique users with activity logs.
     * 
     * @return count of unique users with activity logs
     */
    @Query("SELECT COUNT(DISTINCT ual.userId) FROM UserActivityLog ual")
    long countUniqueUsersWithActivity();

    /**
     * Count activity logs by IP address.
     * 
     * @param ipAddress the IP address
     * @return count of activity logs from the specified IP address
     */
    long countByIpAddress(String ipAddress);

    /**
     * Get most active users (users with most activity logs).
     * 
     * @param limit the maximum number of results
     * @return list of user IDs with activity counts
     */
    @Query("SELECT ual.userId, COUNT(ual) as activityCount FROM UserActivityLog ual " +
           "GROUP BY ual.userId " +
           "ORDER BY activityCount DESC")
    List<Object[]> findMostActiveUsers(@Param("limit") int limit);

    /**
     * Get most common activity types.
     * 
     * @param limit the maximum number of results
     * @return list of activity types with counts
     */
    @Query("SELECT ual.activityType, COUNT(ual) as activityCount FROM UserActivityLog ual " +
           "GROUP BY ual.activityType " +
           "ORDER BY activityCount DESC")
    List<Object[]> findMostCommonActivityTypes(@Param("limit") int limit);

    /**
     * Get activity logs by hour of day.
     * 
     * @return list of hours with activity counts
     */
    @Query("SELECT EXTRACT(HOUR FROM ual.createdAt) as hour, COUNT(ual) as activityCount " +
           "FROM UserActivityLog ual " +
           "GROUP BY EXTRACT(HOUR FROM ual.createdAt) " +
           "ORDER BY hour")
    List<Object[]> findActivityLogsByHour();

    /**
     * Get activity logs by day of week.
     * 
     * @return list of days with activity counts
     */
    @Query("SELECT EXTRACT(DOW FROM ual.createdAt) as dayOfWeek, COUNT(ual) as activityCount " +
           "FROM UserActivityLog ual " +
           "GROUP BY EXTRACT(DOW FROM ual.createdAt) " +
           "ORDER BY dayOfWeek")
    List<Object[]> findActivityLogsByDayOfWeek();
}
