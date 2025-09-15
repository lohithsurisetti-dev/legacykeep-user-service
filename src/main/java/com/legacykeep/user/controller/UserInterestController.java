package com.legacykeep.user.controller;

import com.legacykeep.user.dto.ApiResponse;
import com.legacykeep.user.entity.UserInterest;
import com.legacykeep.user.repository.UserInterestRepository;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for User Interest operations.
 * 
 * Handles all user interest related endpoints.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/interests")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Interest Management", description = "APIs for managing user interests")
public class UserInterestController {

    private final UserInterestRepository userInterestRepository;

    /**
     * Get all user interests.
     */
    @GetMapping
    @Operation(summary = "Get all interests", description = "Retrieve all user interests")
    public ResponseEntity<ApiResponse<List<UserInterest>>> getAllInterests() {
        log.info("Retrieving all user interests");
        
        try {
            List<UserInterest> interests = userInterestRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success(interests, "Interests retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving interests", e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve interests: " + e.getMessage()));
        }
    }

    /**
     * Get user interests by user ID.
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get interests by user ID", description = "Retrieve user interests by user ID")
    public ResponseEntity<ApiResponse<List<UserInterest>>> getInterestsByUserId(@PathVariable Long userId) {
        log.info("Retrieving interests for user ID: {}", userId);
        
        try {
            List<UserInterest> interests = userInterestRepository.findByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success(interests, "Interests retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving interests for user ID: {}", userId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve interests: " + e.getMessage()));
        }
    }

    /**
     * Add user interest.
     */
    @PostMapping
    @Operation(summary = "Add user interest", description = "Add a new interest for a user")
    public ResponseEntity<ApiResponse<UserInterest>> addInterest(@Valid @RequestBody UserInterest interest) {
        log.info("Adding interest for user ID: {}", interest.getUserId());
        
        try {
            // Check if interest already exists for this user
            Optional<UserInterest> existingInterest = userInterestRepository
                .findByUserIdAndInterestName(interest.getUserId(), interest.getInterestName());
            if (existingInterest.isPresent()) {
                return ResponseEntity.status(409)
                    .body(ApiResponse.error("Interest already exists for user: " + interest.getInterestName()));
            }
            
            // Set timestamps
            interest.setCreatedAt(LocalDateTime.now());
            interest.setUpdatedAt(LocalDateTime.now());
            
            UserInterest savedInterest = userInterestRepository.save(interest);
            
            log.info("Interest added successfully with ID: {}", savedInterest.getId());
            return ResponseEntity.status(201)
                .body(ApiResponse.success(savedInterest, "Interest added successfully"));
        } catch (Exception e) {
            log.error("Error adding interest for user ID: {}", interest.getUserId(), e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to add interest: " + e.getMessage()));
        }
    }

    /**
     * Update user interest.
     */
    @PutMapping("/{interestId}")
    @Operation(summary = "Update user interest", description = "Update an existing user interest")
    public ResponseEntity<ApiResponse<UserInterest>> updateInterest(
            @PathVariable Long interestId, 
            @Valid @RequestBody UserInterest updatedInterest) {
        log.info("Updating interest with ID: {}", interestId);
        
        try {
            Optional<UserInterest> existingInterestOpt = userInterestRepository.findById(interestId);
            if (existingInterestOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Interest not found with ID: " + interestId));
            }
            
            UserInterest existingInterest = existingInterestOpt.get();
            
            // Update fields (preserve ID and timestamps)
            existingInterest.setInterestName(updatedInterest.getInterestName());
            existingInterest.setInterestType(updatedInterest.getInterestType());
            existingInterest.setInterestDescription(updatedInterest.getInterestDescription());
            existingInterest.setPublic(updatedInterest.isPublic());
            existingInterest.setUpdatedAt(LocalDateTime.now());
            
            UserInterest savedInterest = userInterestRepository.save(existingInterest);
            
            log.info("Interest updated successfully with ID: {}", savedInterest.getId());
            return ResponseEntity.ok(ApiResponse.success(savedInterest, "Interest updated successfully"));
        } catch (Exception e) {
            log.error("Error updating interest with ID: {}", interestId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to update interest: " + e.getMessage()));
        }
    }

    /**
     * Delete user interest.
     */
    @DeleteMapping("/{interestId}")
    @Operation(summary = "Delete user interest", description = "Delete a user interest")
    public ResponseEntity<ApiResponse<Void>> deleteInterest(@PathVariable Long interestId) {
        log.info("Deleting interest with ID: {}", interestId);
        
        try {
            Optional<UserInterest> interestOpt = userInterestRepository.findById(interestId);
            if (interestOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Interest not found with ID: " + interestId));
            }
            
            userInterestRepository.deleteById(interestId);
            
            log.info("Interest deleted successfully with ID: {}", interestId);
            return ResponseEntity.ok(ApiResponse.success(null, "Interest deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting interest with ID: {}", interestId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to delete interest: " + e.getMessage()));
        }
    }

    /**
     * Get interests by type.
     */
    @GetMapping("/type/{interestType}")
    @Operation(summary = "Get interests by type", description = "Retrieve all interests of a specific type")
    public ResponseEntity<ApiResponse<List<UserInterest>>> getInterestsByType(@PathVariable String interestType) {
        log.info("Retrieving interests of type: {}", interestType);
        
        try {
            List<UserInterest> interests = userInterestRepository.findAll()
                .stream()
                .filter(interest -> interestType.equals(interest.getInterestType()))
                .toList();
            
            return ResponseEntity.ok(ApiResponse.success(interests, "Interests retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving interests of type: {}", interestType, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve interests: " + e.getMessage()));
        }
    }
}
