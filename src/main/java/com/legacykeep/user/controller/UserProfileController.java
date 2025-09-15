package com.legacykeep.user.controller;

import com.legacykeep.user.dto.ApiResponse;
import com.legacykeep.user.dto.request.UserProfileRequestDto;
import com.legacykeep.user.dto.response.UserProfileResponseDto;
import com.legacykeep.user.service.JwtValidationService;
import com.legacykeep.user.service.UserProfileService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * REST Controller for User Profile operations.
 * 
 * Handles all user profile related endpoints with proper authentication
 * and service layer integration.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Profile Management", description = "APIs for managing user profiles")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final JwtValidationService jwtValidationService;

    /**
     * Get current user's profile.
     */
    @GetMapping("/me")
    @Operation(summary = "Get my profile", description = "Retrieve current user's profile")
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> getMyProfile(
            @RequestHeader("Authorization") String authHeader) {
        log.info("Retrieving current user's profile");
        
        Long userId = extractUserIdFromToken(authHeader);
        Optional<UserProfileResponseDto> profile = userProfileService.getProfileByUserId(userId);
        
        if (profile.isEmpty()) {
            return ResponseEntity.status(404)
                .body(ApiResponse.error("Profile not found"));
        }
        
        return ResponseEntity.ok(ApiResponse.success(profile.get(), "Profile retrieved successfully"));
    }

    /**
     * Get user profile by ID.
     */
    @GetMapping("/{profileId}")
    @Operation(summary = "Get profile by ID", description = "Retrieve user profile by ID")
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> getProfileById(@PathVariable("profileId") Long profileId) {
        log.info("Retrieving profile with ID: {}", profileId);
        
        Optional<UserProfileResponseDto> profile = userProfileService.getProfileById(profileId);
        if (profile.isEmpty()) {
            return ResponseEntity.status(404)
                .body(ApiResponse.error("Profile not found with ID: " + profileId));
        }
        
        return ResponseEntity.ok(ApiResponse.success(profile.get(), "Profile retrieved successfully"));
    }

    /**
     * Create a new user profile.
     */
    @PostMapping
    @Operation(summary = "Create user profile", description = "Create a new user profile")
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> createProfile(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody UserProfileRequestDto requestDto) {
        log.info("Creating new user profile");
        
        Long userId = extractUserIdFromToken(authHeader);
        UserProfileResponseDto profile = userProfileService.createProfile(userId, requestDto);
        
        return ResponseEntity.status(201)
            .body(ApiResponse.success(profile, "Profile created successfully"));
    }

    /**
     * Update current user's profile.
     */
    @PutMapping("/me")
    @Operation(summary = "Update my profile", description = "Update current user's profile")
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> updateMyProfile(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody UserProfileRequestDto requestDto) {
        log.info("Updating current user's profile");
        
        Long userId = extractUserIdFromToken(authHeader);
        UserProfileResponseDto profile = userProfileService.updateProfile(userId, requestDto);
        
        return ResponseEntity.ok(ApiResponse.success(profile, "Profile updated successfully"));
    }

    /**
     * Delete current user's profile.
     */
    @DeleteMapping("/me")
    @Operation(summary = "Delete my profile", description = "Delete current user's profile")
    public ResponseEntity<ApiResponse<Void>> deleteMyProfile(
            @RequestHeader("Authorization") String authHeader) {
        log.info("Deleting current user's profile");
        
        Long userId = extractUserIdFromToken(authHeader);
        userProfileService.deleteProfile(userId);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Profile deleted successfully"));
    }

    /**
     * Get public profiles with pagination.
     */
    @GetMapping("/public")
    @Operation(summary = "Get public profiles", description = "Retrieve public user profiles with pagination")
    public ResponseEntity<ApiResponse<Page<UserProfileResponseDto>>> getPublicProfiles(
            Pageable pageable) {
        log.info("Retrieving public profiles with pagination: {}", pageable);
        
        Page<UserProfileResponseDto> profiles = userProfileService.getPublicProfiles(pageable);
        return ResponseEntity.ok(ApiResponse.success(profiles, "Public profiles retrieved successfully"));
    }

    /**
     * Search profiles by name or display name.
     */
    @GetMapping("/search")
    @Operation(summary = "Search profiles", description = "Search profiles by name or display name")
    public ResponseEntity<ApiResponse<Page<UserProfileResponseDto>>> searchProfiles(
            @RequestParam("query") String query,
            Pageable pageable) {
        log.info("Searching profiles with query: {}", query);
        
        Page<UserProfileResponseDto> profiles = userProfileService.searchProfiles(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(profiles, "Search results retrieved successfully"));
    }

    /**
     * Upload profile picture.
     */
    @PostMapping("/me/picture")
    @Operation(summary = "Upload profile picture", description = "Upload a new profile picture")
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> uploadProfilePicture(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file) {
        log.info("Uploading profile picture");
        
        Long userId = extractUserIdFromToken(authHeader);
        
        if (file.isEmpty()) {
            return ResponseEntity.status(400)
                .body(ApiResponse.error("File is empty"));
        }
        
        UserProfileResponseDto profile;
        try {
            profile = userProfileService.uploadProfilePicture(
                userId, file.getBytes(), file.getContentType());
        } catch (IOException e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("Failed to process file: " + e.getMessage()));
        }
        
        return ResponseEntity.ok(ApiResponse.success(profile, "Profile picture uploaded successfully"));
    }

    /**
     * Delete profile picture.
     */
    @DeleteMapping("/me/picture")
    @Operation(summary = "Delete profile picture", description = "Delete current user's profile picture")
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> deleteProfilePicture(
            @RequestHeader("Authorization") String authHeader) {
        log.info("Deleting profile picture");
        
        Long userId = extractUserIdFromToken(authHeader);
        UserProfileResponseDto profile = userProfileService.deleteProfilePicture(userId);
        
        return ResponseEntity.ok(ApiResponse.success(profile, "Profile picture deleted successfully"));
    }

    /**
     * Get profile completion percentage.
     */
    @GetMapping("/me/completion")
    @Operation(summary = "Get profile completion", description = "Get profile completion percentage")
    public ResponseEntity<ApiResponse<Integer>> getProfileCompletion(
            @RequestHeader("Authorization") String authHeader) {
        log.info("Getting profile completion percentage");
        
        Long userId = extractUserIdFromToken(authHeader);
        int completion = userProfileService.getProfileCompletionPercentage(userId);
        
        return ResponseEntity.ok(ApiResponse.success(completion, "Profile completion retrieved successfully"));
    }

    // =============================================================================
    // Private Helper Methods
    // =============================================================================

    /**
     * Extract user ID from JWT token in Authorization header.
     */
    private Long extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        
        String token = authHeader.substring(7);
        return jwtValidationService.extractUserId(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));
    }
}