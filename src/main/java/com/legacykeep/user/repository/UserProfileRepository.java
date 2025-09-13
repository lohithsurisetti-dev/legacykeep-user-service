package com.legacykeep.user.repository;

import com.legacykeep.user.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for UserProfile entity.
 * 
 * Provides basic CRUD operations for user profiles.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * Find user profile by user ID.
     * 
     * @param userId the user ID
     * @return optional user profile
     */
    Optional<UserProfile> findByUserId(Long userId);

    /**
     * Find public profiles that are not deleted.
     * 
     * @param pageable pagination parameters
     * @return page of public profiles
     */
    Page<UserProfile> findByIsPublicTrueAndDeletedAtIsNull(Pageable pageable);

    /**
     * Search profiles by name or display name.
     * 
     * @param query the search query
     * @param pageable pagination parameters
     * @return page of matching profiles
     */
    @Query("SELECT p FROM UserProfile p WHERE " +
           "(LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.displayName) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "p.deletedAt IS NULL")
    Page<UserProfile> searchProfiles(@Param("query") String query, Pageable pageable);
}