package com.legacykeep.user.repository;

import com.legacykeep.user.entity.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for UserPreferences entity.
 * 
 * Provides basic CRUD operations for user preferences.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {

    /**
     * Find user preferences by user ID.
     * 
     * @param userId the user ID
     * @return optional user preferences
     */
    Optional<UserPreferences> findByUserId(Long userId);
}