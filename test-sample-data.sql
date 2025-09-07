-- Test sample data for User Service
-- Insert sample user profile
INSERT INTO user_profiles (user_id, first_name, last_name, display_name, bio, date_of_birth, phone_number, address_line1, city, state, country, postal_code, timezone, language, profile_picture_url, is_public, first_name_hash, last_name_hash, phone_number_hash) 
VALUES (1, 'John', 'Doe', 'Johnny', 'Software developer and family man', '1990-05-15', '+1234567890', '123 Main St', 'New York', 'NY', 'USA', '10001', 'America/New_York', 'en', 'https://example.com/profile.jpg', true, 'hash1', 'hash2', 'hash3');

-- Insert sample user preferences
INSERT INTO user_preferences (user_id, email_notifications, push_notifications, sms_notifications, marketing_emails, profile_visibility, story_visibility, media_visibility, theme_preference, font_size, high_contrast, reduced_motion, auto_play_videos, data_saver_mode)
VALUES (1, true, true, false, false, 'PUBLIC', 'FAMILY_ONLY', 'FAMILY_ONLY', 'DARK', 'LARGE', false, false, true, false);

-- Insert sample user settings
INSERT INTO user_settings (user_id, two_factor_enabled, login_notifications, device_management, data_export_enabled, last_password_change_at, last_profile_update_at, privacy_level, data_retention_days, account_deletion_scheduled_at)
VALUES (1, true, true, true, true, '2025-01-01 00:00:00', '2025-09-07 12:00:00', 'STANDARD', 365, null);

-- Insert sample user interests
INSERT INTO user_interests (user_id, interest_name, interest_type, interest_description, is_public, created_at, updated_at)
VALUES 
(1, 'Photography', 'HOBBY', 'Love taking photos of nature and family', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Cooking', 'HOBBY', 'Enjoy cooking Italian and Asian cuisine', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Technology', 'PROFESSIONAL', 'Software development and AI', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample user activity logs
INSERT INTO user_activity_log (user_id, activity_type, activity_description, ip_address, user_agent, metadata, created_at)
VALUES 
(1, 'PROFILE_CREATED', 'User profile created successfully', '192.168.1.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', '{"source": "registration"}', CURRENT_TIMESTAMP),
(1, 'PROFILE_UPDATED', 'Profile information updated', '192.168.1.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', '{"fields": ["bio", "display_name"]}', CURRENT_TIMESTAMP),
(1, 'PREFERENCES_UPDATED', 'Notification preferences changed', '192.168.1.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', '{"notifications": "email"}', CURRENT_TIMESTAMP);

-- Verify the data
SELECT 'User Profiles' as table_name, count(*) as record_count FROM user_profiles
UNION ALL
SELECT 'User Preferences', count(*) FROM user_preferences
UNION ALL
SELECT 'User Settings', count(*) FROM user_settings
UNION ALL
SELECT 'User Interests', count(*) FROM user_interests
UNION ALL
SELECT 'User Activity Logs', count(*) FROM user_activity_log;
