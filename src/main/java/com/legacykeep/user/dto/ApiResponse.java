package com.legacykeep.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard API response wrapper for all User Service endpoints.
 * 
 * Provides consistent response structure across all API endpoints
 * with proper error handling and metadata.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Indicates if the request was successful.
     */
    private boolean success;

    /**
     * Response message describing the result.
     */
    private String message;

    /**
     * The actual response data.
     */
    private T data;

    /**
     * List of error messages (if any).
     */
    private List<String> errors;

    /**
     * Response timestamp.
     */
    private LocalDateTime timestamp;

    /**
     * Request processing time in milliseconds.
     */
    private Long processingTimeMs;

    /**
     * Additional metadata for the response.
     */
    private Object metadata;

    // =============================================================================
    // Static Factory Methods
    // =============================================================================

    /**
     * Create a successful response with data.
     * 
     * @param data the response data
     * @param <T> the data type
     * @return successful API response
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create a successful response with data and message.
     * 
     * @param data the response data
     * @param message the success message
     * @param <T> the data type
     * @return successful API response
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create a successful response with data, message, and metadata.
     * 
     * @param data the response data
     * @param message the success message
     * @param metadata additional metadata
     * @param <T> the data type
     * @return successful API response
     */
    public static <T> ApiResponse<T> success(T data, String message, Object metadata) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .metadata(metadata)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create an error response with message.
     * 
     * @param message the error message
     * @param <T> the data type
     * @return error API response
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create an error response with message and errors list.
     * 
     * @param message the error message
     * @param errors list of specific errors
     * @param <T> the data type
     * @return error API response
     */
    public static <T> ApiResponse<T> error(String message, List<String> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create an error response with message, errors, and metadata.
     * 
     * @param message the error message
     * @param errors list of specific errors
     * @param metadata additional metadata
     * @param <T> the data type
     * @return error API response
     */
    public static <T> ApiResponse<T> error(String message, List<String> errors, Object metadata) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .metadata(metadata)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create a validation error response.
     * 
     * @param errors list of validation errors
     * @param <T> the data type
     * @return validation error API response
     */
    public static <T> ApiResponse<T> validationError(List<String> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message("Validation failed")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create a not found error response.
     * 
     * @param resource the resource that was not found
     * @param <T> the data type
     * @return not found error API response
     */
    public static <T> ApiResponse<T> notFound(String resource) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(resource + " not found")
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create an unauthorized error response.
     * 
     * @param <T> the data type
     * @return unauthorized error API response
     */
    public static <T> ApiResponse<T> unauthorized() {
        return ApiResponse.<T>builder()
                .success(false)
                .message("Unauthorized access")
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create a forbidden error response.
     * 
     * @param <T> the data type
     * @return forbidden error API response
     */
    public static <T> ApiResponse<T> forbidden() {
        return ApiResponse.<T>builder()
                .success(false)
                .message("Access forbidden")
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create a conflict error response.
     * 
     * @param message the conflict message
     * @param <T> the data type
     * @return conflict error API response
     */
    public static <T> ApiResponse<T> conflict(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create a server error response.
     * 
     * @param message the server error message
     * @param <T> the data type
     * @return server error API response
     */
    public static <T> ApiResponse<T> serverError(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message("Internal server error: " + message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // =============================================================================
    // Builder Methods
    // =============================================================================

    /**
     * Set the processing time for the response.
     * 
     * @param processingTimeMs the processing time in milliseconds
     * @return the API response with processing time set
     */
    public ApiResponse<T> withProcessingTime(Long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
        return this;
    }

    /**
     * Set the metadata for the response.
     * 
     * @param metadata the metadata
     * @return the API response with metadata set
     */
    public ApiResponse<T> withMetadata(Object metadata) {
        this.metadata = metadata;
        return this;
    }
}
