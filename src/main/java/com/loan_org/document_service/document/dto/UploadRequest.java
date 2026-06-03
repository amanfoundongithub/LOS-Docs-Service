package com.loan_org.document_service.document.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequest {

    @NotBlank(message = "Application ID is required")
    private String applicationId;

    @NotBlank(message = "Document type is required (e.g., W2, BANK_STATEMENT)")
    private String documentType;

    @NotBlank(message = "File name is required")
    private String fileName;

    @NotNull(message = "File size is required")
    @Min(value = 1, message = "File size must be greater than 0 bytes")
    private long fileSize;
}