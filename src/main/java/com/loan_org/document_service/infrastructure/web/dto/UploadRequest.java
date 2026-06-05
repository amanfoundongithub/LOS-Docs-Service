package com.loan_org.document_service.infrastructure.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UploadRequest {

    @NotBlank(message = "Application ID cannot be blank")
    private String applicationId;

    @NotNull(message = "Document type is required")
    private String documentType;

    @NotBlank(message = "File name cannot be blank")
    @Pattern(regexp = "(?i)^.+\\.(pdf)$", message = "Only PDF files are allowed")
    private String fileName;

    @NotNull(message = "File size is required")
    @Min(value = 1024, message = "File size must be at least 1 KB (1024 bytes)")
    private Long fileSize;
}