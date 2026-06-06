package com.loan_org.document_service.infrastructure.web.exception;

import com.loan_org.document_service.infrastructure.web.exception.classes.PermissionDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ApiError> handlePermissionDeniedException(PermissionDeniedException ex,
                                                                    HttpServletRequest request) {

        log.warn("[PERMISSION_DENIED] The permission to the resource was denied. Reason : {} for user: {}",
                ex.reason,
                ex.userId);

        ApiError errorMessage = ApiError.builder()
                .timestamp(Instant.now())
                .message(ex.reason)
                .path(ex.endpoint)
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }


}
