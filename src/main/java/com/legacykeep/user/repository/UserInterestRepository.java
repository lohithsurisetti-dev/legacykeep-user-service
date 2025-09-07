package com.legacykeep.user.repository;

import com.legacykeep.user.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserInterest entity.
 * 
 * Provides basic CRUD operations for user interests.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    /**
     * Find user interests by user ID.
     * 
     * @param userId the user ID
     * @return list of user interests
     */
    List<UserInterest> findByUserId(Long userId);

    /**
     * Find user interest by user ID and interest name.
     * 
     * @param userId the user ID
     * @param interestName the interest name
     * @return optional user interest
     */
    Optional<UserInterest> findByUserIdAndInterestName(Long userId, String interestName);
}