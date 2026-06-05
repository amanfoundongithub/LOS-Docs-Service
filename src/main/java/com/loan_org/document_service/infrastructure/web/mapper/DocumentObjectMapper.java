package com.loan_org.document_service.infrastructure.web.mapper;

import com.loan_org.document_service.document.dto.DocumentResponse;
import com.loan_org.document_service.document.dto.UploadDocumentCommand;
import com.loan_org.document_service.document.model.DocumentMetadata;
import com.loan_org.document_service.infrastructure.web.dto.UploadRequest;

public interface DocumentObjectMapper {
    UploadDocumentCommand toCommand(UploadRequest request);
    DocumentResponse toResponse(DocumentMetadata metadata, String uploadUrl);
}
