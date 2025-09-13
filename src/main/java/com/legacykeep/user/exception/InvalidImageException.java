package com.legacykeep.user.exception;

/**
 * Exception thrown when an invalid image is provided.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public class InvalidImageException extends RuntimeException {

    public InvalidImageException(String message) {
        super(message);
    }

    public InvalidImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
