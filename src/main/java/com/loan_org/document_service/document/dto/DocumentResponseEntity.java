package com.loan_org.document_service.document.dto;

import com.loan_org.document_service.document.model.DocumentStatus;
import lombok.Builder;

import java.time.Instant;

/**
 * Defines the document response entity fetched from the database
 *
 * @param storageKey
 * @param documentType
 * @param fileName
 * @param fileSize
 * @param status
 * @param createdAt
 * @param updatedAt
 */
@Builder
public record DocumentResponseEntity(
        String storageKey,
        String documentType,
        String fileName,
        long   fileSize,
        DocumentStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
