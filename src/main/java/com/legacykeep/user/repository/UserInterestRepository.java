package com.legacykeep.user.repository;

import com.legacykeep.user.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserInterest entity operations.
 * 
 * Provides data access methods for user interests management including
 * CRUD operations and interest-based queries.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================

    /**
     * Find user interests by user ID.
     * 
     * @param userId the user ID from auth service
     * @return list of user interests for the user
     */
    List<UserInterest> findByUserId(Long userId);

    /**
     * Find active (non-deleted) user interests by user ID.
     * 
     * @param userId the user ID from auth service
     * @return list of active user interests for the user
     */
    List<UserInterest> findByUserIdAndDeletedAtIsNull(Long userId);

    /**
     * Find public user interests by user ID.
     * 
     * @param userId the user ID from auth service
     * @return list of public user interests for the user
     */
    List<UserInterest> findByUserIdAndIsPublicTrueAndDeletedAtIsNull(Long userId);

    /**
     * Delete all user interests by user ID.
     * 
     * @param userId the user ID from auth service
     */
    void deleteByUserId(Long userId);

    // =============================================================================
    // Interest Type Queries
    // =============================================================================

    /**
     * Find user interests by interest type.
     * 
     * @param interestType the interest type
     * @return list of user interests with the specified type
     */
    List<UserInterest> findByInterestTypeAndDeletedAtIsNull(UserInterest.InterestType interestType);

    /**
     * Find user interests by interest type and user ID.
     * 
     * @param userId the user ID from auth service
     * @param interestType the interest type
     * @return list of user interests with the specified type for the user
     */
    List<UserInterest> findByUserIdAndInterestTypeAndDeletedAtIsNull(Long userId, UserInterest.InterestType interestType);

    /**
     * Find public user interests by interest type.
     * 
     * @param interestType the interest type
     * @return list of public user interests with the specified type
     */
    List<UserInterest> findByInterestTypeAndIsPublicTrueAndDeletedAtIsNull(UserInterest.InterestType interestType);

    // =============================================================================
    // Interest Name Queries
    // =============================================================================

    /**
     * Find user interests by interest name (case-insensitive).
     * 
     * @param interestName the interest name to search for
     * @return list of user interests with the specified name
     */
    List<UserInterest> findByInterestNameContainingIgnoreCaseAndDeletedAtIsNull(String interestName);

    /**
     * Find user interests by exact interest name.
     * 
     * @param interestName the exact interest name
     * @return list of user interests with the exact name
     */
    List<UserInterest> findByInterestNameAndDeletedAtIsNull(String interestName);

    /**
     * Find user interests by interest name and type.
     * 
     * @param interestName the interest name
     * @param interestType the interest type
     * @return list of user interests with the specified name and type
     */
    List<UserInterest> findByInterestNameAndInterestTypeAndDeletedAtIsNull(String interestName, UserInterest.InterestType interestType);

    // =============================================================================
    // Public/Private Queries
    // =============================================================================

    /**
     * Find all public user interests.
     * 
     * @return list of all public user interests
     */
    List<UserInterest> findByIsPublicTrueAndDeletedAtIsNull();

    /**
     * Find all private user interests.
     * 
     * @return list of all private user interests
     */
    List<UserInterest> findByIsPublicFalseAndDeletedAtIsNull();

    // =============================================================================
    // Status-based Queries
    // =============================================================================

    /**
     * Find all active (non-deleted) user interests.
     * 
     * @return list of all active user interests
     */
    List<UserInterest> findByDeletedAtIsNull();

    /**
     * Find all deleted user interests.
     * 
     * @return list of all deleted user interests
     */
    List<UserInterest> findByDeletedAtIsNotNull();

    /**
     * Find user interests created after a specific date.
     * 
     * @param date the date to filter by
     * @return list of user interests created after the date
     */
    List<UserInterest> findByCreatedAtAfterAndDeletedAtIsNull(LocalDateTime date);

    /**
     * Find user interests updated after a specific date.
     * 
     * @param date the date to filter by
     * @return list of user interests updated after the date
     */
    List<UserInterest> findByUpdatedAtAfterAndDeletedAtIsNull(LocalDateTime date);

    // =============================================================================
    // Custom Queries
    // =============================================================================

    /**
     * Find users with specific interest.
     * 
     * @param interestName the interest name
     * @param interestType the interest type
     * @return list of user IDs with the specified interest
     */
    @Query("SELECT ui.userId FROM UserInterest ui WHERE " +
           "ui.interestName = :interestName AND " +
           "ui.interestType = :interestType AND " +
           "ui.deletedAt IS NULL")
    List<Long> findUserIdsWithInterest(@Param("interestName") String interestName, 
                                     @Param("interestType") UserInterest.InterestType interestType);

    /**
     * Find users with interests in a specific category.
     * 
     * @param interestType the interest type
     * @return list of user IDs with interests in the specified category
     */
    @Query("SELECT DISTINCT ui.userId FROM UserInterest ui WHERE " +
           "ui.interestType = :interestType AND " +
           "ui.deletedAt IS NULL")
    List<Long> findUserIdsWithInterestType(@Param("interestType") UserInterest.InterestType interestType);

    /**
     * Find popular interests (most common interests).
     * 
     * @param limit the maximum number of results
     * @return list of popular interest names with counts
     */
    @Query("SELECT ui.interestName, COUNT(ui) as interestCount FROM UserInterest ui WHERE " +
           "ui.deletedAt IS NULL " +
           "GROUP BY ui.interestName " +
           "ORDER BY interestCount DESC")
    List<Object[]> findPopularInterests(@Param("limit") int limit);

    /**
     * Find popular interests by type.
     * 
     * @param interestType the interest type
     * @param limit the maximum number of results
     * @return list of popular interest names with counts for the specified type
     */
    @Query("SELECT ui.interestName, COUNT(ui) as interestCount FROM UserInterest ui WHERE " +
           "ui.interestType = :interestType AND " +
           "ui.deletedAt IS NULL " +
           "GROUP BY ui.interestName " +
           "ORDER BY interestCount DESC")
    List<Object[]> findPopularInterestsByType(@Param("interestType") UserInterest.InterestType interestType, 
                                            @Param("limit") int limit);

    /**
     * Find users with similar interests.
     * 
     * @param userId the user ID to find similar users for
     * @param limit the maximum number of results
     * @return list of user IDs with similar interests
     */
    @Query("SELECT ui2.userId, COUNT(ui2) as commonInterests FROM UserInterest ui1 " +
           "JOIN UserInterest ui2 ON ui1.interestName = ui2.interestName AND ui1.interestType = ui2.interestType " +
           "WHERE ui1.userId = :userId AND ui2.userId != :userId AND ui1.deletedAt IS NULL AND ui2.deletedAt IS NULL " +
           "GROUP BY ui2.userId " +
           "ORDER BY commonInterests DESC")
    List<Object[]> findUsersWithSimilarInterests(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * Find interests by description (case-insensitive search).
     * 
     * @param description the description to search for
     * @return list of user interests with matching descriptions
     */
    @Query("SELECT ui FROM UserInterest ui WHERE " +
           "ui.interestDescription ILIKE %:description% AND " +
           "ui.deletedAt IS NULL")
    List<UserInterest> findByDescriptionContaining(@Param("description") String description);

    // =============================================================================
    // Analytics Queries
    // =============================================================================

    /**
     * Count total active user interests.
     * 
     * @return count of active user interests
     */
    @Query("SELECT COUNT(ui) FROM UserInterest ui WHERE ui.deletedAt IS NULL")
    long countActiveInterests();

    /**
     * Count user interests by type.
     * 
     * @param interestType the interest type
     * @return count of user interests with the specified type
     */
    long countByInterestTypeAndDeletedAtIsNull(UserInterest.InterestType interestType);

    /**
     * Count public user interests.
     * 
     * @return count of public user interests
     */
    @Query("SELECT COUNT(ui) FROM UserInterest ui WHERE ui.isPublic = true AND ui.deletedAt IS NULL")
    long countPublicInterests();

    /**
     * Count private user interests.
     * 
     * @return count of private user interests
     */
    @Query("SELECT COUNT(ui) FROM UserInterest ui WHERE ui.isPublic = false AND ui.deletedAt IS NULL")
    long countPrivateInterests();

    /**
     * Count unique interest names.
     * 
     * @return count of unique interest names
     */
    @Query("SELECT COUNT(DISTINCT ui.interestName) FROM UserInterest ui WHERE ui.deletedAt IS NULL")
    long countUniqueInterestNames();

    /**
     * Count users with interests.
     * 
     * @return count of users who have at least one interest
     */
    @Query("SELECT COUNT(DISTINCT ui.userId) FROM UserInterest ui WHERE ui.deletedAt IS NULL")
    long countUsersWithInterests();

    /**
     * Get average number of interests per user.
     * 
     * @return average number of interests per user
     */
    @Query("SELECT AVG(interestCount) FROM (" +
           "SELECT COUNT(ui) as interestCount FROM UserInterest ui WHERE ui.deletedAt IS NULL GROUP BY ui.userId" +
           ")")
    Double getAverageInterestsPerUser();

    /**
     * Count interests created in date range.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return count of interests created in the date range
     */
    @Query("SELECT COUNT(ui) FROM UserInterest ui WHERE ui.createdAt BETWEEN :startDate AND :endDate")
    long countInterestsCreatedBetween(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
}
