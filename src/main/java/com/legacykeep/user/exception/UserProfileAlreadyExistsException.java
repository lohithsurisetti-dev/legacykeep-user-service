package com.legacykeep.user.exception;

/**
 * Exception thrown when trying to create a profile that already exists.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public class UserProfileAlreadyExistsException extends RuntimeException {

    public UserProfileAlreadyExistsException(String message) {
        super(message);
    }

    public UserProfileAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
