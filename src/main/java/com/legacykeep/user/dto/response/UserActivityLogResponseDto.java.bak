package com.legacykeep.user.dto.response;

import com.legacykeep.user.entity.UserActivityLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user activity log response data.
 * 
 * Contains user activity log information returned by the API
 * including activity type, description, and metadata.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityLogResponseDto {

    private Long id;
    private Long userId;
    private UserActivityLog.ActivityType activityType;
    private String activityDescription;
    private String ipAddress;
    private String userAgent;
    private String deviceInfo;
    private String locationInfo;
    private String metadata;

    // Computed Properties
    private String displayType;
    private boolean isSecurityActivity;
    private boolean isProfileActivity;
    private boolean isPreferenceActivity;
    private boolean isDataActivity;
    private long ageInMinutes;
    private long ageInHours;
    private long ageInDays;

    private LocalDateTime createdAt;
}
