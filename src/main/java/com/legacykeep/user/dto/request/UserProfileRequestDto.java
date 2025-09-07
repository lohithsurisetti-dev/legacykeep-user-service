package com.legacykeep.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for user profile creation and update requests.
 * 
 * Contains validation rules for user profile data including
 * personal information, contact details, and profile settings.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
public class UserProfileRequestDto {

    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "First name can only contain letters, spaces, hyphens, and apostrophes")
    private String firstName;

    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Last name can only contain letters, spaces, hyphens, and apostrophes")
    private String lastName;

    @Size(max = 100, message = "Display name must not exceed 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s'-._]+$", message = "Display name can only contain letters, numbers, spaces, hyphens, apostrophes, dots, and underscores")
    private String displayName;

    @Size(max = 2000, message = "Bio must not exceed 2000 characters")
    private String bio;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]+$", message = "Phone number can only contain digits, spaces, hyphens, parentheses, and plus sign")
    private String phoneNumber;

    @Size(max = 255, message = "Address line 1 must not exceed 255 characters")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "City can only contain letters, spaces, hyphens, and apostrophes")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "State can only contain letters, spaces, hyphens, and apostrophes")
    private String state;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Country can only contain letters, spaces, hyphens, and apostrophes")
    private String country;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "Postal code can only contain letters, numbers, spaces, and hyphens")
    private String postalCode;

    @Size(max = 50, message = "Timezone must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9/_-]+$", message = "Timezone can only contain letters, numbers, slashes, underscores, and hyphens")
    private String timezone;

    @Size(max = 10, message = "Language must not exceed 10 characters")
    @Pattern(regexp = "^[a-z]{2}(-[A-Z]{2})?$", message = "Language must be in format 'en' or 'en-US'")
    private String language;

    private boolean isPublic = false;
}
