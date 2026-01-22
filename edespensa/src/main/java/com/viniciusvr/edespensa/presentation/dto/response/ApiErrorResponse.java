package com.viniciusvr.edespensa.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Generic API error response.
 */
public record ApiErrorResponse(
    int status,
    String error,
    String message,
    String path,
    LocalDateTime timestamp,
    Map<String, String> validationErrors
) {
    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return new ApiErrorResponse(status, error, message, path, LocalDateTime.now(), null);
    }
    
    public static ApiErrorResponse withValidation(int status, String error, String message, 
                                                  String path, Map<String, String> validationErrors) {
        return new ApiErrorResponse(status, error, message, path, LocalDateTime.now(), validationErrors);
    }
}
