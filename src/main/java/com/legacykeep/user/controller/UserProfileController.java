package com.legacykeep.user.controller;

import com.legacykeep.user.dto.ApiResponse;
import com.legacykeep.user.entity.UserProfile;
import com.legacykeep.user.repository.UserProfileRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for User Profile operations.
 * 
 * Handles all user profile related endpoints.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Profile Management", description = "APIs for managing user profiles")
public class UserProfileController {

    private final UserProfileRepository userProfileRepository;

    /**
     * Get all user profiles.
     */
    @GetMapping
    @Operation(summary = "Get all profiles", description = "Retrieve all user profiles")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getAllProfiles() {
        log.info("Retrieving all user profiles");
        
        try {
            List<UserProfile> profiles = userProfileRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success(profiles, "Profiles retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving profiles", e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve profiles: " + e.getMessage()));
        }
    }

    /**
     * Get user profile by ID.
     */
    @GetMapping("/{profileId}")
    @Operation(summary = "Get profile by ID", description = "Retrieve user profile by ID")
    public ResponseEntity<ApiResponse<UserProfile>> getProfileById(@PathVariable Long profileId) {
        log.info("Retrieving profile with ID: {}", profileId);
        
        try {
            Optional<UserProfile> profileOpt = userProfileRepository.findById(profileId);
            if (profileOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Profile not found with ID: " + profileId));
            }
            
            return ResponseEntity.ok(ApiResponse.success(profileOpt.get(), "Profile retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving profile with ID: {}", profileId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve profile: " + e.getMessage()));
        }
    }

    /**
     * Get user profile by user ID.
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get profile by user ID", description = "Retrieve user profile by user ID")
    public ResponseEntity<ApiResponse<UserProfile>> getProfileByUserId(@PathVariable Long userId) {
        log.info("Retrieving profile for user ID: {}", userId);
        
        try {
            Optional<UserProfile> profileOpt = userProfileRepository.findByUserId(userId);
            if (profileOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Profile not found for user ID: " + userId));
            }
            
            return ResponseEntity.ok(ApiResponse.success(profileOpt.get(), "Profile retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving profile for user ID: {}", userId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve profile: " + e.getMessage()));
        }
    }

    /**
     * Create a new user profile.
     */
    @PostMapping
    @Operation(summary = "Create user profile", description = "Create a new user profile")
    public ResponseEntity<ApiResponse<UserProfile>> createProfile(@RequestBody UserProfile profile) {
        log.info("Creating new user profile for user ID: {}", profile.getUserId());
        
        try {
            // Check if profile already exists for this user
            Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(profile.getUserId());
            if (existingProfile.isPresent()) {
                return ResponseEntity.status(409)
                    .body(ApiResponse.error("Profile already exists for user ID: " + profile.getUserId()));
            }
            
            // Set timestamps
            profile.setCreatedAt(LocalDateTime.now());
            profile.setUpdatedAt(LocalDateTime.now());
            
            UserProfile savedProfile = userProfileRepository.save(profile);
            
            log.info("Profile created successfully with ID: {}", savedProfile.getId());
            return ResponseEntity.status(201)
                .body(ApiResponse.success(savedProfile, "Profile created successfully"));
        } catch (Exception e) {
            log.error("Error creating profile for user ID: {}", profile.getUserId(), e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to create profile: " + e.getMessage()));
        }
    }

    /**
     * Update user profile.
     */
    @PutMapping("/{profileId}")
    @Operation(summary = "Update user profile", description = "Update an existing user profile")
    public ResponseEntity<ApiResponse<UserProfile>> updateProfile(
            @PathVariable Long profileId, 
            @RequestBody UserProfile updatedProfile) {
        log.info("Updating profile with ID: {}", profileId);
        
        try {
            Optional<UserProfile> existingProfileOpt = userProfileRepository.findById(profileId);
            if (existingProfileOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Profile not found with ID: " + profileId));
            }
            
            UserProfile existingProfile = existingProfileOpt.get();
            
            // Update fields (preserve ID and timestamps)
            existingProfile.setFirstName(updatedProfile.getFirstName());
            existingProfile.setLastName(updatedProfile.getLastName());
            existingProfile.setDisplayName(updatedProfile.getDisplayName());
            existingProfile.setBio(updatedProfile.getBio());
            existingProfile.setDateOfBirth(updatedProfile.getDateOfBirth());
            existingProfile.setPhoneNumber(updatedProfile.getPhoneNumber());
            existingProfile.setAddressLine1(updatedProfile.getAddressLine1());
            existingProfile.setAddressLine2(updatedProfile.getAddressLine2());
            existingProfile.setCity(updatedProfile.getCity());
            existingProfile.setState(updatedProfile.getState());
            existingProfile.setCountry(updatedProfile.getCountry());
            existingProfile.setPostalCode(updatedProfile.getPostalCode());
            existingProfile.setTimezone(updatedProfile.getTimezone());
            existingProfile.setLanguage(updatedProfile.getLanguage());
            existingProfile.setProfilePictureUrl(updatedProfile.getProfilePictureUrl());
            existingProfile.setProfilePictureThumbnailUrl(updatedProfile.getProfilePictureThumbnailUrl());
            existingProfile.setPublic(updatedProfile.isPublic());
            existingProfile.setUpdatedAt(LocalDateTime.now());
            
            UserProfile savedProfile = userProfileRepository.save(existingProfile);
            
            log.info("Profile updated successfully with ID: {}", savedProfile.getId());
            return ResponseEntity.ok(ApiResponse.success(savedProfile, "Profile updated successfully"));
        } catch (Exception e) {
            log.error("Error updating profile with ID: {}", profileId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to update profile: " + e.getMessage()));
        }
    }

    /**
     * Delete user profile.
     */
    @DeleteMapping("/{profileId}")
    @Operation(summary = "Delete user profile", description = "Delete a user profile")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@PathVariable Long profileId) {
        log.info("Deleting profile with ID: {}", profileId);
        
        try {
            Optional<UserProfile> profileOpt = userProfileRepository.findById(profileId);
            if (profileOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("Profile not found with ID: " + profileId));
            }
            
            userProfileRepository.deleteById(profileId);
            
            log.info("Profile deleted successfully with ID: {}", profileId);
            return ResponseEntity.ok(ApiResponse.success(null, "Profile deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting profile with ID: {}", profileId, e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to delete profile: " + e.getMessage()));
        }
    }

    /**
     * Get public profiles only.
     */
    @GetMapping("/public")
    @Operation(summary = "Get public profiles", description = "Retrieve only public user profiles")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getPublicProfiles(
            @RequestParam(defaultValue = "20") int limit) {
        log.info("Retrieving public profiles with limit: {}", limit);
        
        try {
            List<UserProfile> profiles = userProfileRepository.findAll()
                .stream()
                .filter(UserProfile::isPublic)
                .limit(limit)
                .toList();
            
            return ResponseEntity.ok(ApiResponse.success(profiles, "Public profiles retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving public profiles", e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to retrieve public profiles: " + e.getMessage()));
        }
    }

    /**
     * Create a test profile.
     */
    @PostMapping("/create-test")
    @Operation(summary = "Create test profile", description = "Create a simple test profile")
    public ResponseEntity<ApiResponse<UserProfile>> createTestProfile() {
        log.info("Creating test profile");
        
        try {
            UserProfile profile = new UserProfile();
            profile.setUserId(999L); // Test user ID
            profile.setFirstName("Test");
            profile.setLastName("User");
            profile.setDisplayName("Test User");
            profile.setBio("This is a test profile");
            profile.setCreatedAt(LocalDateTime.now());
            profile.setUpdatedAt(LocalDateTime.now());
            
            UserProfile savedProfile = userProfileRepository.save(profile);
            
            log.info("Test profile created with ID: {}", savedProfile.getId());
            return ResponseEntity.status(201)
                .body(ApiResponse.success(savedProfile, "Test profile created successfully"));
        } catch (Exception e) {
            log.error("Error creating test profile", e);
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to create test profile: " + e.getMessage()));
        }
    }
}
