package com.epw.skillswap.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ApiError {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> fieldErrors;

    public ApiError(int status, String error, String message, LocalDateTime timestamp) {
        this(status, error, message, timestamp, null);
    }

    public ApiError(int status, String error, String message, LocalDateTime timestamp, Map<String, String> fieldErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
        this.fieldErrors = fieldErrors;
    }
}