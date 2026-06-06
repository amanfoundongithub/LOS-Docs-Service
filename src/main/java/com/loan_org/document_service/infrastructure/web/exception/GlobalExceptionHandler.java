package com.loan_org.document_service.infrastructure.web.exception;

import com.loan_org.document_service.document.exception.DocumentNotFoundException;
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

        ApiError errorMessage = constructErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), ex.endpoint);
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ApiError> handleDocumentNotFoundException(DocumentNotFoundException ex,
                                                                    HttpServletRequest request) {

        log.warn("[DOCUMENT_NOT_FOUND] The document was not found for the key: {} & value: {}",
                ex.key,
                ex.value);

        ApiError errorMessage = constructErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "/api/v1/internal/minio-callback");
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex,
                                                    HttpServletRequest request) {
        log.warn("[INTERNAL_SERVER_ERROR] An internal server error occurred while processing the request.");
        ApiError errorMessage = constructErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "path");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    private ApiError constructErrorResponse(HttpStatus status, String message, String path) {
        return ApiError.builder()
                .timestamp(Instant.now())
                .message(message)
                .path(path)
                .status(status.value())
                .error(status.getReasonPhrase())
                .build();
    }


}
