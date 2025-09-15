 package com.legacykeep.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for updating user profile picture.
 * 
 * Contains the profile picture URL and thumbnail URL.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
public class UpdateProfilePictureRequestDto {

    @Size(max = 500, message = "Profile picture URL must not exceed 500 characters")
    private String profilePictureUrl;

    @Size(max = 500, message = "Profile picture thumbnail URL must not exceed 500 characters")
    private String profilePictureThumbnailUrl;
}
