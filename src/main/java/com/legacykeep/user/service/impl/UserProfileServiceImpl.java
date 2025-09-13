package com.legacykeep.user.service.impl;

import com.legacykeep.user.entity.UserProfile;
import com.legacykeep.user.dto.request.UserProfileRequestDto;
import com.legacykeep.user.dto.response.UserProfileResponseDto;
import com.legacykeep.user.exception.InvalidImageException;
import com.legacykeep.user.exception.UserProfileAlreadyExistsException;
import com.legacykeep.user.exception.UserProfileNotFoundException;
import com.legacykeep.user.repository.UserProfileRepository;
import com.legacykeep.user.service.AuthServiceIntegration;
import com.legacykeep.user.service.UserProfileService;
import com.legacykeep.user.service.UserEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of User Profile Service.
 * 
 * Handles all user profile operations including CRUD operations,
 * profile picture management, and profile validation.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserEventService userEventService;
    private final AuthServiceIntegration authServiceIntegration;
    private final String uploadPath = "/tmp/legacykeep/user-uploads";

    @Override
    public UserProfileResponseDto createProfile(Long userId, UserProfileRequestDto requestDto) {
        log.info("Creating profile for user ID: {}", userId);

        // Validate user exists in Auth Service
        if (!authServiceIntegration.validateUserExists(userId)) {
            throw new IllegalArgumentException("User not found in Auth Service: " + userId);
        }

        // Check if profile already exists
        if (profileExists(userId)) {
            throw new UserProfileAlreadyExistsException("Profile already exists for user ID: " + userId);
        }

        // Create new profile
        UserProfile profile = new UserProfile();
        profile.setUserId(userId);
        profile.setFirstName(requestDto.getFirstName());
        profile.setLastName(requestDto.getLastName());
        profile.setDisplayName(requestDto.getDisplayName());
        profile.setBio(requestDto.getBio());
        profile.setDateOfBirth(requestDto.getDateOfBirth());
        profile.setPhoneNumber(requestDto.getPhoneNumber());
        profile.setAddressLine1(requestDto.getAddressLine1());
        profile.setAddressLine2(requestDto.getAddressLine2());
        profile.setCity(requestDto.getCity());
        profile.setState(requestDto.getState());
        profile.setCountry(requestDto.getCountry());
        profile.setPostalCode(requestDto.getPostalCode());
        profile.setTimezone(requestDto.getTimezone());
        profile.setLanguage(requestDto.getLanguage());
        profile.setPublic(requestDto.isPublic());
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());

        // Generate hashes for searchable fields
        generateHashes(profile);

        UserProfile savedProfile = userProfileRepository.save(profile);

        // Publish profile created event
        userEventService.publishProfileCreatedEvent(savedProfile);

        log.info("Profile created successfully with ID: {}", savedProfile.getId());
        return mapToResponseDto(savedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileResponseDto> getProfileByUserId(Long userId) {
        log.debug("Retrieving profile for user ID: {}", userId);
        
        return userProfileRepository.findByUserId(userId)
                .map(this::mapToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileResponseDto> getProfileById(Long profileId) {
        log.debug("Retrieving profile with ID: {}", profileId);
        
        return userProfileRepository.findById(profileId)
                .map(this::mapToResponseDto);
    }

    @Override
    public UserProfileResponseDto updateProfile(Long userId, UserProfileRequestDto requestDto) {
        log.info("Updating profile for user ID: {}", userId);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user ID: " + userId));

        // Update fields
        profile.setFirstName(requestDto.getFirstName());
        profile.setLastName(requestDto.getLastName());
        profile.setDisplayName(requestDto.getDisplayName());
        profile.setBio(requestDto.getBio());
        profile.setDateOfBirth(requestDto.getDateOfBirth());
        profile.setPhoneNumber(requestDto.getPhoneNumber());
        profile.setAddressLine1(requestDto.getAddressLine1());
        profile.setAddressLine2(requestDto.getAddressLine2());
        profile.setCity(requestDto.getCity());
        profile.setState(requestDto.getState());
        profile.setCountry(requestDto.getCountry());
        profile.setPostalCode(requestDto.getPostalCode());
        profile.setTimezone(requestDto.getTimezone());
        profile.setLanguage(requestDto.getLanguage());
        profile.setPublic(requestDto.isPublic());
        profile.setUpdatedAt(LocalDateTime.now());

        // Regenerate hashes
        generateHashes(profile);

        UserProfile savedProfile = userProfileRepository.save(profile);

        // Publish profile updated event
        userEventService.publishProfileUpdatedEvent(savedProfile);

        log.info("Profile updated successfully for user ID: {}", userId);
        return mapToResponseDto(savedProfile);
    }

    @Override
    public void deleteProfile(Long userId) {
        log.info("Deleting profile for user ID: {}", userId);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user ID: " + userId));

        // Soft delete
        profile.softDelete();
        userProfileRepository.save(profile);

        // Publish profile deleted event
        userEventService.publishProfileDeletedEvent(profile);

        log.info("Profile deleted successfully for user ID: {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserProfileResponseDto> getPublicProfiles(Pageable pageable) {
        log.debug("Retrieving public profiles with pagination: {}", pageable);
        
        return userProfileRepository.findByIsPublicTrueAndDeletedAtIsNull(pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserProfileResponseDto> searchProfiles(String query, Pageable pageable) {
        log.debug("Searching profiles with query: {}", query);
        
        return userProfileRepository.searchProfiles(query, pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    public UserProfileResponseDto uploadProfilePicture(Long userId, byte[] imageData, String contentType) {
        log.info("Uploading profile picture for user ID: {}", userId);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user ID: " + userId));

        try {
            // Validate image
            validateImage(imageData, contentType);

            // Generate unique filename
            String filename = UUID.randomUUID().toString() + getFileExtension(contentType);
            Path filePath = Paths.get(uploadPath, filename);

            // Ensure directory exists
            Files.createDirectories(filePath.getParent());

            // Save file
            Files.write(filePath, imageData);

            // Update profile with new picture URLs
            String baseUrl = "http://localhost:8082/user/api/v1/profiles/pictures";
            profile.setProfilePictureUrl(baseUrl + "/" + filename);
            profile.setProfilePictureThumbnailUrl(baseUrl + "/thumbnails/" + filename);
            profile.setUpdatedAt(LocalDateTime.now());

            UserProfile savedProfile = userProfileRepository.save(profile);

            // Publish profile picture updated event
            userEventService.publishProfilePictureUpdatedEvent(savedProfile);

            log.info("Profile picture uploaded successfully for user ID: {}", userId);
            return mapToResponseDto(savedProfile);

        } catch (IOException e) {
            log.error("Error uploading profile picture for user ID: {}", userId, e);
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }

    @Override
    public UserProfileResponseDto deleteProfilePicture(Long userId) {
        log.info("Deleting profile picture for user ID: {}", userId);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user ID: " + userId));

        // Remove picture URLs
        profile.setProfilePictureUrl(null);
        profile.setProfilePictureThumbnailUrl(null);
        profile.setUpdatedAt(LocalDateTime.now());

        UserProfile savedProfile = userProfileRepository.save(profile);

        // Publish profile picture deleted event
        userEventService.publishProfilePictureDeletedEvent(savedProfile);

        log.info("Profile picture deleted successfully for user ID: {}", userId);
        return mapToResponseDto(savedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean profileExists(Long userId) {
        return userProfileRepository.findByUserId(userId).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public int getProfileCompletionPercentage(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user ID: " + userId));

        int totalFields = 8; // firstName, lastName, bio, dateOfBirth, phoneNumber, city, country, profilePicture
        int completedFields = 0;

        if (profile.getFirstName() != null && !profile.getFirstName().trim().isEmpty()) completedFields++;
        if (profile.getLastName() != null && !profile.getLastName().trim().isEmpty()) completedFields++;
        if (profile.getBio() != null && !profile.getBio().trim().isEmpty()) completedFields++;
        if (profile.getDateOfBirth() != null) completedFields++;
        if (profile.getPhoneNumber() != null && !profile.getPhoneNumber().trim().isEmpty()) completedFields++;
        if (profile.getCity() != null && !profile.getCity().trim().isEmpty()) completedFields++;
        if (profile.getCountry() != null && !profile.getCountry().trim().isEmpty()) completedFields++;
        if (profile.getProfilePictureUrl() != null && !profile.getProfilePictureUrl().trim().isEmpty()) completedFields++;

        return (completedFields * 100) / totalFields;
    }

    // =============================================================================
    // Private Helper Methods
    // =============================================================================

    private UserProfileResponseDto mapToResponseDto(UserProfile profile) {
        UserProfileResponseDto responseDto = new UserProfileResponseDto();
        responseDto.setId(profile.getId());
        responseDto.setUserId(profile.getUserId());
        responseDto.setFirstName(profile.getFirstName());
        responseDto.setLastName(profile.getLastName());
        responseDto.setDisplayName(profile.getDisplayName());
        responseDto.setBio(profile.getBio());
        responseDto.setDateOfBirth(profile.getDateOfBirth());
        responseDto.setPhoneNumber(profile.getPhoneNumber());
        responseDto.setAddressLine1(profile.getAddressLine1());
        responseDto.setAddressLine2(profile.getAddressLine2());
        responseDto.setCity(profile.getCity());
        responseDto.setState(profile.getState());
        responseDto.setCountry(profile.getCountry());
        responseDto.setPostalCode(profile.getPostalCode());
        responseDto.setTimezone(profile.getTimezone());
        responseDto.setLanguage(profile.getLanguage());
        responseDto.setProfilePictureUrl(profile.getProfilePictureUrl());
        responseDto.setProfilePictureThumbnailUrl(profile.getProfilePictureThumbnailUrl());
        responseDto.setPublic(profile.isPublic());
        responseDto.setCreatedAt(profile.getCreatedAt());
        responseDto.setUpdatedAt(profile.getUpdatedAt());
        responseDto.setFullName(profile.getFullName());
        responseDto.setAge(profile.getAge());
        responseDto.setComplete(profile.isComplete());
        return responseDto;
    }

    private void generateHashes(UserProfile profile) {
        if (profile.getFirstName() != null) {
            profile.setFirstNameHash(hashString(profile.getFirstName()));
        }
        if (profile.getLastName() != null) {
            profile.setLastNameHash(hashString(profile.getLastName()));
        }
        if (profile.getPhoneNumber() != null) {
            profile.setPhoneNumberHash(hashString(profile.getPhoneNumber()));
        }
    }

    private String hashString(String input) {
        return String.valueOf(input.hashCode());
    }

    private void validateImage(byte[] imageData, String contentType) {
        // Check file size (5MB limit)
        if (imageData.length > 5 * 1024 * 1024) {
            throw new InvalidImageException("Image size exceeds 5MB limit");
        }

        // Check content type
        if (!isValidImageType(contentType)) {
            throw new InvalidImageException("Invalid image type: " + contentType);
        }
    }

    private boolean isValidImageType(String contentType) {
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/webp")
        );
    }

    private String getFileExtension(String contentType) {
        switch (contentType) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "image/webp":
                return ".webp";
            default:
                return ".jpg";
        }
    }
}
