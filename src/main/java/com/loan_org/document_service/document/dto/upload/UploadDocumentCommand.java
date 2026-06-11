package com.loan_org.document_service.document.dto.upload;

import com.loan_org.document_service.document.decorator.ValidContentType;
import com.loan_org.document_service.document.model.DocumentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Defines a record class to store the configuration related to uploading
 * of document in our domain class.
 *
 * @param applicationId The application ID of the loan
 * @param customerId    The unique ID of the applicant owning this document
 * @param documentType  The type of document we are uploading (e.g., AADHAAR, PAN)
 * @param contentType   The verified MIME type (e.g., application/pdf, image/png)
 * @param fileName      Name of the file we are uploading
 * @param fileSize      Size of the file we are uploading in bytes
 *
 * @author amanfoundongithub
 * @version 2.0.0
 */
public record UploadDocumentCommand(

        @NotBlank(message = "Application ID is required, to connect with loan.")
        String       applicationId,

        @NotBlank(message = "Customer ID is required for audit purposes.")
        String       customerId,

        @NotNull(message = "Document type is required to proceed for validation.")
        DocumentType documentType,

        @NotBlank(message = "Content Type is required to save the document.")
        @ValidContentType
        String       contentType,

        @NotBlank(message = "File name is required to save the file.")
        String       fileName,

        @Min(value = 1, message = "File size must be greater than 0 bytes.")
        long         fileSize
) {}
