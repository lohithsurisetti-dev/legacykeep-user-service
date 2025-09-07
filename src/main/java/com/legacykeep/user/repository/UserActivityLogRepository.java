package com.legacykeep.user.repository;

import com.legacykeep.user.entity.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for UserActivityLog entity.
 * 
 * Provides basic CRUD operations for user activity logs.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {

    /**
     * Find user activity logs by user ID.
     * 
     * @param userId the user ID
     * @return list of user activity logs
     */
    List<UserActivityLog> findByUserId(Long userId);
}