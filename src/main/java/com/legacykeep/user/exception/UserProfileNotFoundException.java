package com.legacykeep.user.exception;

/**
 * Exception thrown when a user profile is not found.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public class UserProfileNotFoundException extends RuntimeException {

    public UserProfileNotFoundException(String message) {
        super(message);
    }

    public UserProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
