package com.loan_org.document_service.document.mapper;

import com.loan_org.document_service.document.dto.DocumentResponse;
import com.loan_org.document_service.document.dto.DocumentUploadResponse;
import com.loan_org.document_service.document.dto.UploadRequest;
import com.loan_org.document_service.document.model.DocumentMetadata;

public interface DocumentObjectMapper {

    // Helpers to handle initialization of request
    DocumentMetadata       mapToDocumentMetaData(UploadRequest uploadRequest, String storageKey);
    DocumentUploadResponse mapToDocumentUploadResponse(DocumentMetadata metadata, String presignedURL);

    // Other helpers
    DocumentResponse       mapToDocumentResponse(DocumentMetadata metadata, String presignedURL);
}
