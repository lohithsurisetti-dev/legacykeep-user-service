package com.legacykeep.user.service;

import com.legacykeep.user.entity.UserProfile;

/**
 * Service interface for publishing user-related events.
 * 
 * Handles event publishing to Kafka for user profile changes,
 * preferences updates, and other user-related activities.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public interface UserEventService {

    /**
     * Publish profile created event.
     * 
     * @param profile the created profile
     */
    void publishProfileCreatedEvent(UserProfile profile);

    /**
     * Publish profile updated event.
     * 
     * @param profile the updated profile
     */
    void publishProfileUpdatedEvent(UserProfile profile);

    /**
     * Publish profile deleted event.
     * 
     * @param profile the deleted profile
     */
    void publishProfileDeletedEvent(UserProfile profile);

    /**
     * Publish profile picture updated event.
     * 
     * @param profile the profile with updated picture
     */
    void publishProfilePictureUpdatedEvent(UserProfile profile);

    /**
     * Publish profile picture deleted event.
     * 
     * @param profile the profile with deleted picture
     */
    void publishProfilePictureDeletedEvent(UserProfile profile);

    /**
     * Publish preferences updated event.
     * 
     * @param userId the user ID
     * @param preferencesType the type of preferences updated
     */
    void publishPreferencesUpdatedEvent(Long userId, String preferencesType);

    /**
     * Publish settings updated event.
     * 
     * @param userId the user ID
     * @param settingsType the type of settings updated
     */
    void publishSettingsUpdatedEvent(Long userId, String settingsType);

    /**
     * Publish user activity event.
     * 
     * @param userId the user ID
     * @param activityType the type of activity
     * @param activityDescription the activity description
     */
    void publishUserActivityEvent(Long userId, String activityType, String activityDescription);
}
