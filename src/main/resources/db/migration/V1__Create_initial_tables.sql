-- =============================================================================
-- LegacyKeep User Service - Initial Database Schema
-- Migration: V1__Create_initial_tables.sql
-- Description: Creates the initial database schema for user profiles and preferences
-- =============================================================================

-- User Profiles table (Personal information and bio)
CREATE TABLE user_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE, -- References auth service user ID
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    display_name VARCHAR(100),
    bio TEXT,
    date_of_birth DATE,
    phone_number VARCHAR(20),
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    timezone VARCHAR(50) DEFAULT 'UTC',
    language VARCHAR(10) DEFAULT 'en',
    profile_picture_url VARCHAR(500),
    profile_picture_thumbnail_url VARCHAR(500),
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- User Preferences table (Settings and preferences)
CREATE TABLE user_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE, -- References auth service user ID
    email_notifications BOOLEAN DEFAULT TRUE,
    push_notifications BOOLEAN DEFAULT TRUE,
    sms_notifications BOOLEAN DEFAULT FALSE,
    marketing_emails BOOLEAN DEFAULT FALSE,
    profile_visibility VARCHAR(20) DEFAULT 'PRIVATE', -- PUBLIC, PRIVATE, FRIENDS_ONLY
    story_visibility VARCHAR(20) DEFAULT 'FAMILY_ONLY', -- PUBLIC, FAMILY_ONLY, PRIVATE
    media_visibility VARCHAR(20) DEFAULT 'FAMILY_ONLY', -- PUBLIC, FAMILY_ONLY, PRIVATE
    theme_preference VARCHAR(20) DEFAULT 'LIGHT', -- LIGHT, DARK, AUTO
    font_size VARCHAR(20) DEFAULT 'MEDIUM', -- SMALL, MEDIUM, LARGE, EXTRA_LARGE
    high_contrast BOOLEAN DEFAULT FALSE,
    reduced_motion BOOLEAN DEFAULT FALSE,
    auto_play_videos BOOLEAN DEFAULT TRUE,
    data_saver_mode BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- User Settings table (Account and security settings)
CREATE TABLE user_settings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE, -- References auth service user ID
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    login_notifications BOOLEAN DEFAULT TRUE,
    device_management BOOLEAN DEFAULT TRUE,
    data_export_enabled BOOLEAN DEFAULT TRUE,
    account_deletion_scheduled_at TIMESTAMP,
    last_password_change_at TIMESTAMP,
    last_profile_update_at TIMESTAMP,
    privacy_level VARCHAR(20) DEFAULT 'STANDARD', -- MINIMAL, STANDARD, MAXIMUM
    data_retention_days INTEGER DEFAULT 2555, -- 7 years default
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- User Interests table (Tags and preferences)
CREATE TABLE user_interests (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- References auth service user ID
    interest_type VARCHAR(50) NOT NULL, -- HOBBY, SKILL, INTEREST, PASSION
    interest_name VARCHAR(100) NOT NULL,
    interest_description TEXT,
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- User Activity Log table (Track user actions for analytics)
CREATE TABLE user_activity_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- References auth service user ID
    activity_type VARCHAR(50) NOT NULL, -- PROFILE_UPDATE, PREFERENCE_CHANGE, LOGIN, etc.
    activity_description TEXT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    device_info TEXT,
    location_info TEXT,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- Indexes for Performance
-- =============================================================================

-- User Profiles indexes
CREATE INDEX idx_user_profiles_user_id ON user_profiles(user_id);
CREATE INDEX idx_user_profiles_display_name ON user_profiles(display_name);
CREATE INDEX idx_user_profiles_is_public ON user_profiles(is_public);
CREATE INDEX idx_user_profiles_created_at ON user_profiles(created_at);
CREATE INDEX idx_user_profiles_deleted_at ON user_profiles(deleted_at);

-- User Preferences indexes
CREATE INDEX idx_user_preferences_user_id ON user_preferences(user_id);
CREATE INDEX idx_user_preferences_profile_visibility ON user_preferences(profile_visibility);
CREATE INDEX idx_user_preferences_theme ON user_preferences(theme_preference);

-- User Settings indexes
CREATE INDEX idx_user_settings_user_id ON user_settings(user_id);
CREATE INDEX idx_user_settings_two_factor ON user_settings(two_factor_enabled);
CREATE INDEX idx_user_settings_privacy_level ON user_settings(privacy_level);

-- User Interests indexes
CREATE INDEX idx_user_interests_user_id ON user_interests(user_id);
CREATE INDEX idx_user_interests_type ON user_interests(interest_type);
CREATE INDEX idx_user_interests_name ON user_interests(interest_name);
CREATE INDEX idx_user_interests_is_public ON user_interests(is_public);
CREATE INDEX idx_user_interests_deleted_at ON user_interests(deleted_at);

-- User Activity Log indexes
CREATE INDEX idx_user_activity_log_user_id ON user_activity_log(user_id);
CREATE INDEX idx_user_activity_log_type ON user_activity_log(activity_type);
CREATE INDEX idx_user_activity_log_created_at ON user_activity_log(created_at);

-- =============================================================================
-- Constraints
-- =============================================================================

-- Add foreign key constraints (commented out since we're referencing auth service)
-- ALTER TABLE user_profiles ADD CONSTRAINT fk_user_profiles_user_id 
--     FOREIGN KEY (user_id) REFERENCES auth_db.users(id) ON DELETE CASCADE;

-- ALTER TABLE user_preferences ADD CONSTRAINT fk_user_preferences_user_id 
--     FOREIGN KEY (user_id) REFERENCES auth_db.users(id) ON DELETE CASCADE;

-- ALTER TABLE user_settings ADD CONSTRAINT fk_user_settings_user_id 
--     FOREIGN KEY (user_id) REFERENCES auth_db.users(id) ON DELETE CASCADE;

-- ALTER TABLE user_interests ADD CONSTRAINT fk_user_interests_user_id 
--     FOREIGN KEY (user_id) REFERENCES auth_db.users(id) ON DELETE CASCADE;

-- ALTER TABLE user_activity_log ADD CONSTRAINT fk_user_activity_log_user_id 
--     FOREIGN KEY (user_id) REFERENCES auth_db.users(id) ON DELETE CASCADE;

-- =============================================================================
-- Comments
-- =============================================================================

COMMENT ON TABLE user_profiles IS 'User personal information and profile data';
COMMENT ON TABLE user_preferences IS 'User preferences and settings for notifications and privacy';
COMMENT ON TABLE user_settings IS 'User account and security settings';
COMMENT ON TABLE user_interests IS 'User interests, hobbies, and skills';
COMMENT ON TABLE user_activity_log IS 'User activity tracking for analytics and security';

COMMENT ON COLUMN user_profiles.user_id IS 'References the user ID from the auth service';
COMMENT ON COLUMN user_preferences.user_id IS 'References the user ID from the auth service';
COMMENT ON COLUMN user_settings.user_id IS 'References the user ID from the auth service';
COMMENT ON COLUMN user_interests.user_id IS 'References the user ID from the auth service';
COMMENT ON COLUMN user_activity_log.user_id IS 'References the user ID from the auth service';
