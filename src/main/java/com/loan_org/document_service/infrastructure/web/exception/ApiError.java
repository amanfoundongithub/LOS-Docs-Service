package com.loan_org.document_service.infrastructure.web.exception;

import lombok.Builder;

import java.time.Instant;

/**
 * Define a clean error message for the user to understand how the error is coming up!
 *
 * @param timestamp
 * @param status
 * @param error
 * @param message
 * @param path
 */
@Builder
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
){}