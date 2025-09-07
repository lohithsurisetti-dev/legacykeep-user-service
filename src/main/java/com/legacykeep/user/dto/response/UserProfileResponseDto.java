package com.legacykeep.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for user profile response data.
 * 
 * Contains user profile information returned by the API
 * including personal details, contact information, and profile settings.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String displayName;
    private String fullName;
    private String bio;
    private LocalDate dateOfBirth;
    private Integer age;
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
    private boolean isComplete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}
