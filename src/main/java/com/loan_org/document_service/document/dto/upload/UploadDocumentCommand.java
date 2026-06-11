package com.loan_org.document_service.document.dto.upload;

import com.loan_org.document_service.document.constraints.ValidationConstants;
import com.loan_org.document_service.document.constraints.decorator.ValidateContentType;
import com.loan_org.document_service.document.model.DocumentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * This is a record class that stores all the variables requested by the {@link com.loan_org.document_service.document.service.DocumentService Document Service}
 * to service the upload functionality for the document. It contains all the meta-data parameters
 * related to the saving of the document.
 *
 * <p>
 *     <strong>(For HTTP model, refer to {@link com.loan_org.document_service.infrastructure.web.dto.UploadRequest UploadRequest}.)</strong>
 * </p>
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

        @NotBlank(message = ValidationConstants.APPLICATION_ID_NOT_BLANK_MESSAGE)
        String       applicationId,

        @NotBlank(message = ValidationConstants.CUSTOMER_ID_NOT_BLANK_MESSAGE)
        String       customerId,

        @NotNull(message = ValidationConstants.DOCUMENT_TYPE_NOT_BLANK_MESSAGE)
        DocumentType documentType,

        @NotBlank(message = ValidationConstants.CONTENT_TYPE_NOT_BLANK_MESSAGE)
        @ValidateContentType(message = ValidationConstants.CONTENT_TYPE_NOT_VALID_MESSAGE)
        String       contentType,

        @NotBlank(message = ValidationConstants.FILE_NAME_NOT_BLANK_MESSAGE)
        String       fileName,

        @Min(value = ValidationConstants.MIN_FILE_SIZE_IN_BYTES,
                message = ValidationConstants.FILE_MINIMUM_SIZE_MESSAGE)
        long         fileSize
) {}
