package com.legacykeep.user.dto.request;

import com.legacykeep.user.entity.UserInterest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for user interest creation and update requests.
 * 
 * Contains validation rules for user interests including
 * interest type, name, description, and visibility settings.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
public class UserInterestRequestDto {

    @NotNull(message = "Interest type is required")
    private UserInterest.InterestType interestType;

    @NotBlank(message = "Interest name is required")
    @Size(max = 100, message = "Interest name must not exceed 100 characters")
    private String interestName;

    @Size(max = 500, message = "Interest description must not exceed 500 characters")
    private String interestDescription;

    private boolean isPublic = false;
}
