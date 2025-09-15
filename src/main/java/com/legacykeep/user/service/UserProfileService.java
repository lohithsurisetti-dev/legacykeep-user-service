package com.legacykeep.user.service;

import com.legacykeep.user.dto.request.UpdateProfilePictureRequestDto;
import com.legacykeep.user.dto.request.UserProfileRequestDto;
import com.legacykeep.user.dto.response.UserProfileResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service interface for User Profile operations.
 * 
 * Defines the contract for user profile management including CRUD operations,
 * profile picture management, and profile validation.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public interface UserProfileService {

    /**
     * Create a new user profile.
     * 
     * @param userId the user ID from auth service
     * @param requestDto the profile creation request
     * @return the created profile response
     */
    UserProfileResponseDto createProfile(Long userId, UserProfileRequestDto requestDto);

    /**
     * Get user profile by user ID.
     * 
     * @param userId the user ID
     * @return the profile response if found
     */
    Optional<UserProfileResponseDto> getProfileByUserId(Long userId);

    /**
     * Get user profile by profile ID.
     * 
     * @param profileId the profile ID
     * @return the profile response if found
     */
    Optional<UserProfileResponseDto> getProfileById(Long profileId);

    /**
     * Update user profile.
     * 
     * @param userId the user ID
     * @param requestDto the profile update request
     * @return the updated profile response
     */
    UserProfileResponseDto updateProfile(Long userId, UserProfileRequestDto requestDto);

    /**
     * Update user profile picture.
     * 
     * @param userId the user ID
     * @param requestDto the profile picture update request
     * @return the updated profile response
     */
    UserProfileResponseDto updateProfilePicture(Long userId, UpdateProfilePictureRequestDto requestDto);

    /**
     * Delete user profile (soft delete).
     * 
     * @param userId the user ID
     */
    void deleteProfile(Long userId);

    /**
     * Get public profiles with pagination.
     * 
     * @param pageable pagination parameters
     * @return page of public profiles
     */
    Page<UserProfileResponseDto> getPublicProfiles(Pageable pageable);

    /**
     * Search profiles by name or display name.
     * 
     * @param query the search query
     * @param pageable pagination parameters
     * @return page of matching profiles
     */
    Page<UserProfileResponseDto> searchProfiles(String query, Pageable pageable);

    /**
     * Upload profile picture.
     * 
     * @param userId the user ID
     * @param imageData the image data
     * @param contentType the content type
     * @return the updated profile with new picture URL
     */
    UserProfileResponseDto uploadProfilePicture(Long userId, byte[] imageData, String contentType);

    /**
     * Delete profile picture.
     * 
     * @param userId the user ID
     * @return the updated profile without picture
     */
    UserProfileResponseDto deleteProfilePicture(Long userId);

    /**
     * Check if profile exists for user.
     * 
     * @param userId the user ID
     * @return true if profile exists
     */
    boolean profileExists(Long userId);

    /**
     * Get profile completion percentage.
     * 
     * @param userId the user ID
     * @return completion percentage (0-100)
     */
    int getProfileCompletionPercentage(Long userId);
}
