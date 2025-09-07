package com.legacykeep.user.repository;

import com.legacykeep.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
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
}