package com.loan_org.document_service.infrastructure.web.dto.upload;

import com.loan_org.document_service.document.constraints.ValidationConstants;
import com.loan_org.document_service.document.constraints.decorator.ValidateContentType;
import com.loan_org.document_service.document.model.DocumentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentUploadHttpRequest {

    @NotBlank(message = ValidationConstants.APPLICATION_ID_NOT_BLANK_MESSAGE)
    private String applicationId;

    @NotNull(message = ValidationConstants.DOCUMENT_TYPE_NOT_BLANK_MESSAGE)
    private DocumentType documentType;

    @NotBlank(message = ValidationConstants.CUSTOMER_ID_NOT_BLANK_MESSAGE)
    private String       customerId;

    @NotBlank(message = ValidationConstants.CONTENT_TYPE_NOT_BLANK_MESSAGE)
    @ValidateContentType(message = ValidationConstants.CONTENT_TYPE_NOT_VALID_MESSAGE)
    private String       contentType;

    @NotBlank(message = ValidationConstants.FILE_NAME_NOT_BLANK_MESSAGE)
    private String fileName;

    @Min(value = ValidationConstants.MIN_FILE_SIZE_IN_BYTES,
            message = ValidationConstants.FILE_MINIMUM_SIZE_MESSAGE)
    private Long fileSize;
}