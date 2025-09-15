package com.legacykeep.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * Request DTO for User Profile operations.
 * 
 * Contains all fields that can be updated in a user profile.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
public class UserProfileRequestDto {

    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\-']+$", message = "First name can only contain letters, spaces, hyphens, and apostrophes")
    private String firstName;

    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\-']+$", message = "Last name can only contain letters, spaces, hyphens, and apostrophes")
    private String lastName;

    @Size(max = 100, message = "Display name must not exceed 100 characters")
    private String displayName;

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^[+]?[0-9\\s\\-\\(\\)]{7,20}$", message = "Phone number must be a valid phone number")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    @Size(max = 255, message = "Address line 1 must not exceed 255 characters")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-]{3,20}$", message = "Postal code must be a valid postal code")
    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Pattern(regexp = "^[a-zA-Z_/]+$", message = "Timezone must be a valid timezone identifier")
    @Size(max = 50, message = "Timezone must not exceed 50 characters")
    private String timezone;

    @Pattern(regexp = "^[a-z]{2}(-[A-Z]{2})?$", message = "Language must be a valid language code (e.g., en, en-US)")
    @Size(max = 10, message = "Language must not exceed 10 characters")
    private String language;

    @Size(max = 500, message = "Profile picture URL must not exceed 500 characters")
    private String profilePictureUrl;

    @Size(max = 500, message = "Profile picture thumbnail URL must not exceed 500 characters")
    private String profilePictureThumbnailUrl;

    private boolean isPublic = false;
}