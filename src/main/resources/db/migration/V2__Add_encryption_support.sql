-- =============================================================================
-- LegacyKeep User Service - Add Encryption Support
-- Migration: V2__Add_encryption_support.sql
-- Description: Adds encryption support for sensitive user data
-- =============================================================================

-- Add hash columns for efficient searching of encrypted fields
ALTER TABLE user_profiles ADD COLUMN first_name_hash VARCHAR(64);
ALTER TABLE user_profiles ADD COLUMN last_name_hash VARCHAR(64);
ALTER TABLE user_profiles ADD COLUMN phone_number_hash VARCHAR(64);

-- Add unique indexes for hash columns
CREATE UNIQUE INDEX idx_user_profiles_first_name_hash ON user_profiles(first_name_hash) WHERE first_name_hash IS NOT NULL;
CREATE UNIQUE INDEX idx_user_profiles_last_name_hash ON user_profiles(last_name_hash) WHERE last_name_hash IS NOT NULL;
CREATE UNIQUE INDEX idx_user_profiles_phone_number_hash ON user_profiles(phone_number_hash) WHERE phone_number_hash IS NOT NULL;

-- Add comments for documentation
COMMENT ON COLUMN user_profiles.first_name_hash IS 'SHA-256 hash of first name for efficient encrypted field searches';
COMMENT ON COLUMN user_profiles.last_name_hash IS 'SHA-256 hash of last name for efficient encrypted field searches';
COMMENT ON COLUMN user_profiles.phone_number_hash IS 'SHA-256 hash of phone number for efficient encrypted field searches';

-- Note: The actual encryption/decryption is handled by the EncryptedStringConverter
-- in the application layer. This migration documents the hash columns for search optimization.
