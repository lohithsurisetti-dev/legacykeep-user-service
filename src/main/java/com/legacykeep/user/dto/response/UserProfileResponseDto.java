package com.legacykeep.user.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for User Profile operations.
 * 
 * Contains all profile information that can be returned to clients.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
public class UserProfileResponseDto {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String displayName;
    private String bio;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String timezone;
    private String language;
    private String profilePictureUrl;
    private String profilePictureThumbnailUrl;
    private boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Computed fields
    private String fullName;
    private Integer age;
    private boolean isComplete;
}