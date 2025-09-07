package com.legacykeep.user.dto.response;

import com.legacykeep.user.entity.UserInterest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user interest response data.
 * 
 * Contains user interest information returned by the API
 * including interest type, name, description, and visibility settings.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInterestResponseDto {

    private Long id;
    private Long userId;
    private UserInterest.InterestType interestType;
    private String interestName;
    private String interestDescription;
    private boolean isPublic;

    // Computed Properties
    private String displayType;
    private String fullDescription;
    private boolean isSkill;
    private boolean isHobby;
    private boolean isPassion;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
