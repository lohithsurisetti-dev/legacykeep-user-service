package com.legacykeep.user.repository;

import com.legacykeep.user.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for UserSettings entity.
 * 
 * Provides basic CRUD operations for user settings.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

    /**
     * Find user settings by user ID.
     * 
     * @param userId the user ID
     * @return optional user settings
     */
    Optional<UserSettings> findByUserId(Long userId);
}