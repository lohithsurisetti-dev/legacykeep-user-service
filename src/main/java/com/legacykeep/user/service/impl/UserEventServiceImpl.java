package com.legacykeep.user.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legacykeep.user.entity.UserProfile;
import com.legacykeep.user.service.UserEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of User Event Service.
 * 
 * Publishes user-related events to Kafka for other services to consume.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventServiceImpl implements UserEventService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    // Kafka topics
    private static final String USER_PROFILE_TOPIC = "user.profile.events";
    private static final String USER_PREFERENCES_TOPIC = "user.preferences.events";
    private static final String USER_SETTINGS_TOPIC = "user.settings.events";
    private static final String USER_ACTIVITY_TOPIC = "user.activity.events";

    @Override
    public void publishProfileCreatedEvent(UserProfile profile) {
        try {
            Map<String, Object> event = createProfileEvent("PROFILE_CREATED", profile);
            String eventJson = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send(USER_PROFILE_TOPIC, eventJson);
            log.info("Published profile created event for user ID: {}", profile.getUserId());
        } catch (Exception e) {
            log.error("Error publishing profile created event for user ID: {}", profile.getUserId(), e);
        }
    }

    @Override
    public void publishProfileUpdatedEvent(UserProfile profile) {
        try {
            Map<String, Object> event = createProfileEvent("PROFILE_UPDATED", profile);
            String eventJson = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send(USER_PROFILE_TOPIC, eventJson);
            log.info("Published profile updated event for user ID: {}", profile.getUserId());
        } catch (Exception e) {
            log.error("Error publishing profile updated event for user ID: {}", profile.getUserId(), e);
        }
    }

    @Override
    public void publishProfileDeletedEvent(UserProfile profile) {
        try {
            Map<String, Object> event = createProfileEvent("PROFILE_DELETED", profile);
            String eventJson = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send(USER_PROFILE_TOPIC, eventJson);
            log.info("Published profile deleted event for user ID: {}", profile.getUserId());
        } catch (Exception e) {
            log.error("Error publishing profile deleted event for user ID: {}", profile.getUserId(), e);
        }
    }

    @Override
    public void publishProfilePictureUpdatedEvent(UserProfile profile) {
        try {
            Map<String, Object> event = createProfileEvent("PROFILE_PICTURE_UPDATED", profile);
            String eventJson = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send(USER_PROFILE_TOPIC, eventJson);
            log.info("Published profile picture updated event for user ID: {}", profile.getUserId());
        } catch (Exception e) {
            log.error("Error publishing profile picture updated event for user ID: {}", profile.getUserId(), e);
        }
    }

    @Override
    public void publishProfilePictureDeletedEvent(UserProfile profile) {
        try {
            Map<String, Object> event = createProfileEvent("PROFILE_PICTURE_DELETED", profile);
            String eventJson = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send(USER_PROFILE_TOPIC, eventJson);
            log.info("Published profile picture deleted event for user ID: {}", profile.getUserId());
        } catch (Exception e) {
            log.error("Error publishing profile picture deleted event for user ID: {}", profile.getUserId(), e);
        }
    }

    @Override
    public void publishPreferencesUpdatedEvent(Long userId, String preferencesType) {
        try {
            Map<String, Object> event = createGenericEvent("PREFERENCES_UPDATED", userId, preferencesType);
            String eventJson = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send(USER_PREFERENCES_TOPIC, eventJson);
            log.info("Published preferences updated event for user ID: {} type: {}", userId, preferencesType);
        } catch (Exception e) {
            log.error("Error publishing preferences updated event for user ID: {}", userId, e);
        }
    }

    @Override
    public void publishSettingsUpdatedEvent(Long userId, String settingsType) {
        try {
            Map<String, Object> event = createGenericEvent("SETTINGS_UPDATED", userId, settingsType);
            String eventJson = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send(USER_SETTINGS_TOPIC, eventJson);
            log.info("Published settings updated event for user ID: {} type: {}", userId, settingsType);
        } catch (Exception e) {
            log.error("Error publishing settings updated event for user ID: {}", userId, e);
        }
    }

    @Override
    public void publishUserActivityEvent(Long userId, String activityType, String activityDescription) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "USER_ACTIVITY");
            event.put("userId", userId);
            event.put("activityType", activityType);
            event.put("activityDescription", activityDescription);
            event.put("timestamp", LocalDateTime.now());
            event.put("service", "user-service");
            
            String eventJson = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send(USER_ACTIVITY_TOPIC, eventJson);
            log.info("Published user activity event for user ID: {} type: {}", userId, activityType);
        } catch (Exception e) {
            log.error("Error publishing user activity event for user ID: {}", userId, e);
        }
    }

    // =============================================================================
    // Private Helper Methods
    // =============================================================================

    private Map<String, Object> createProfileEvent(String eventType, UserProfile profile) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", eventType);
        event.put("userId", profile.getUserId());
        event.put("profileId", profile.getId());
        event.put("timestamp", LocalDateTime.now());
        event.put("service", "user-service");
        
        // Include profile data (excluding sensitive information)
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("displayName", profile.getDisplayName());
        profileData.put("isPublic", profile.isPublic());
        profileData.put("profilePictureUrl", profile.getProfilePictureUrl());
        profileData.put("city", profile.getCity());
        profileData.put("country", profile.getCountry());
        profileData.put("language", profile.getLanguage());
        profileData.put("timezone", profile.getTimezone());
        
        event.put("profileData", profileData);
        
        return event;
    }

    private Map<String, Object> createGenericEvent(String eventType, Long userId, String type) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", eventType);
        event.put("userId", userId);
        event.put("type", type);
        event.put("timestamp", LocalDateTime.now());
        event.put("service", "user-service");
        
        return event;
    }
}
