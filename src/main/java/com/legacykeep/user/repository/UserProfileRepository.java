package com.legacykeep.user.repository;

import com.legacykeep.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserProfile entity operations.
 * 
 * Provides data access methods for user profile management including
 * CRUD operations, custom queries, and search functionality.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================

    /**
     * Find user profile by user ID.
     * 
     * @param userId the user ID from auth service
     * @return Optional containing the user profile if found
     */
    Optional<UserProfile> findByUserId(Long userId);

    /**
     * Check if user profile exists by user ID.
     * 
     * @param userId the user ID from auth service
     * @return true if profile exists
     */
    boolean existsByUserId(Long userId);

    /**
     * Delete user profile by user ID.
     * 
     * @param userId the user ID from auth service
     */
    void deleteByUserId(Long userId);

    // =============================================================================
    // Search Operations
    // =============================================================================

    /**
     * Find user profiles by display name (case-insensitive).
     * 
     * @param displayName the display name to search for
     * @return list of matching user profiles
     */
    List<UserProfile> findByDisplayNameContainingIgnoreCaseAndDeletedAtIsNull(String displayName);

    /**
     * Find public user profiles.
     * 
     * @return list of public user profiles
     */
    List<UserProfile> findByIsPublicTrueAndDeletedAtIsNull();

    /**
     * Find user profiles by first name hash (for encrypted field searches).
     * 
     * @param firstNameHash the hash of the first name
     * @return Optional containing the user profile if found
     */
    Optional<UserProfile> findByFirstNameHash(String firstNameHash);

    /**
     * Find user profiles by last name hash (for encrypted field searches).
     * 
     * @param lastNameHash the hash of the last name
     * @return Optional containing the user profile if found
     */
    Optional<UserProfile> findByLastNameHash(String lastNameHash);

    /**
     * Find user profiles by phone number hash (for encrypted field searches).
     * 
     * @param phoneNumberHash the hash of the phone number
     * @return Optional containing the user profile if found
     */
    Optional<UserProfile> findByPhoneNumberHash(String phoneNumberHash);

    /**
     * Check if first name hash exists.
     * 
     * @param firstNameHash the hash of the first name
     * @return true if hash exists
     */
    boolean existsByFirstNameHash(String firstNameHash);

    /**
     * Check if last name hash exists.
     * 
     * @param lastNameHash the hash of the last name
     * @return true if hash exists
     */
    boolean existsByLastNameHash(String lastNameHash);

    /**
     * Check if phone number hash exists.
     * 
     * @param phoneNumberHash the hash of the phone number
     * @return true if hash exists
     */
    boolean existsByPhoneNumberHash(String phoneNumberHash);

    // =============================================================================
    // Status-based Queries
    // =============================================================================

    /**
     * Find all active (non-deleted) user profiles.
     * 
     * @return list of active user profiles
     */
    List<UserProfile> findByDeletedAtIsNull();

    /**
     * Find all deleted user profiles.
     * 
     * @return list of deleted user profiles
     */
    List<UserProfile> findByDeletedAtIsNotNull();

    /**
     * Find user profiles created after a specific date.
     * 
     * @param date the date to filter by
     * @return list of user profiles created after the date
     */
    List<UserProfile> findByCreatedAtAfterAndDeletedAtIsNull(LocalDateTime date);

    /**
     * Find user profiles updated after a specific date.
     * 
     * @param date the date to filter by
     * @return list of user profiles updated after the date
     */
    List<UserProfile> findByUpdatedAtAfterAndDeletedAtIsNull(LocalDateTime date);

    // =============================================================================
    // Custom Queries
    // =============================================================================

    /**
     * Find user profiles by location (city, state, country).
     * 
     * @param city the city to search for
     * @param state the state to search for
     * @param country the country to search for
     * @return list of matching user profiles
     */
    @Query("SELECT up FROM UserProfile up WHERE " +
           "(:city IS NULL OR up.city ILIKE %:city%) AND " +
           "(:state IS NULL OR up.state ILIKE %:state%) AND " +
           "(:country IS NULL OR up.country ILIKE %:country%) AND " +
           "up.deletedAt IS NULL")
    List<UserProfile> findByLocation(@Param("city") String city, 
                                    @Param("state") String state, 
                                    @Param("country") String country);

    /**
     * Find user profiles by age range.
     * 
     * @param minAge the minimum age
     * @param maxAge the maximum age
     * @return list of user profiles within the age range
     */
    @Query("SELECT up FROM UserProfile up WHERE " +
           "up.dateOfBirth IS NOT NULL AND " +
           "up.deletedAt IS NULL AND " +
           "EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM up.dateOfBirth) BETWEEN :minAge AND :maxAge")
    List<UserProfile> findByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge);

    /**
     * Find user profiles with incomplete information.
     * 
     * @return list of user profiles missing essential information
     */
    @Query("SELECT up FROM UserProfile up WHERE " +
           "up.deletedAt IS NULL AND " +
           "(up.firstName IS NULL OR up.firstName = '' OR " +
           "up.lastName IS NULL OR up.lastName = '' OR " +
           "up.bio IS NULL OR up.bio = '')")
    List<UserProfile> findIncompleteProfiles();

    /**
     * Count user profiles by creation date range.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return count of user profiles created in the date range
     */
    @Query("SELECT COUNT(up) FROM UserProfile up WHERE up.createdAt BETWEEN :startDate AND :endDate")
    long countProfilesCreatedBetween(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);

    /**
     * Find user profiles with profile pictures.
     * 
     * @return list of user profiles with profile pictures
     */
    @Query("SELECT up FROM UserProfile up WHERE " +
           "up.profilePictureUrl IS NOT NULL AND " +
           "up.profilePictureUrl != '' AND " +
           "up.deletedAt IS NULL")
    List<UserProfile> findProfilesWithPictures();

    /**
     * Find user profiles by timezone.
     * 
     * @param timezone the timezone to search for
     * @return list of user profiles in the specified timezone
     */
    List<UserProfile> findByTimezoneAndDeletedAtIsNull(String timezone);

    /**
     * Find user profiles by language.
     * 
     * @param language the language to search for
     * @return list of user profiles with the specified language
     */
    List<UserProfile> findByLanguageAndDeletedAtIsNull(String language);

    // =============================================================================
    // Analytics Queries
    // =============================================================================

    /**
     * Count total active user profiles.
     * 
     * @return count of active user profiles
     */
    @Query("SELECT COUNT(up) FROM UserProfile up WHERE up.deletedAt IS NULL")
    long countActiveProfiles();

    /**
     * Count public user profiles.
     * 
     * @return count of public user profiles
     */
    @Query("SELECT COUNT(up) FROM UserProfile up WHERE up.isPublic = true AND up.deletedAt IS NULL")
    long countPublicProfiles();

    /**
     * Count user profiles with profile pictures.
     * 
     * @return count of user profiles with profile pictures
     */
    @Query("SELECT COUNT(up) FROM UserProfile up WHERE " +
           "up.profilePictureUrl IS NOT NULL AND " +
           "up.profilePictureUrl != '' AND " +
           "up.deletedAt IS NULL")
    long countProfilesWithPictures();
}
