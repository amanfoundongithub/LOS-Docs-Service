package com.loan_org.document_service.document.mapper;

import com.loan_org.document_service.document.dto.DocumentResponse;
import com.loan_org.document_service.document.dto.DocumentUploadResponse;
import com.loan_org.document_service.document.dto.UploadRequest;
import com.loan_org.document_service.document.model.DocumentMetadata;

public interface DocumentObjectMapper {
    DocumentMetadata mapToDocumentMetaData(UploadRequest uploadRequest, String storageKey);
    DocumentResponse mapToDocumentResponse(DocumentMetadata metadata, String presignedURL);

    DocumentUploadResponse mapToDocumentUploadResponse(DocumentMetadata metadata, String presignedURL);
}
