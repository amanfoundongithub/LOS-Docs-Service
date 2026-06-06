package com.loan_org.document_service.infrastructure.web.dto;

import com.loan_org.document_service.document.model.DocumentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class DocumentResponse {
    private String documentType;
    private String fileName;
    private long fileSize;
    private DocumentStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private String storageKey;
}