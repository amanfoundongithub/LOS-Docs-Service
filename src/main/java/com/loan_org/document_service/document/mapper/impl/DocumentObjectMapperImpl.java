package com.loan_org.document_service.document.mapper.impl;

import com.loan_org.document_service.document.dto.DocumentResponse;
import com.loan_org.document_service.document.dto.DocumentUploadResponse;
import com.loan_org.document_service.document.dto.UploadRequest;
import com.loan_org.document_service.document.mapper.DocumentObjectMapper;
import com.loan_org.document_service.document.model.DocumentMetadata;
import com.loan_org.document_service.document.model.DocumentStatus;
import org.springframework.stereotype.Service;

@Service
public class DocumentObjectMapperImpl implements DocumentObjectMapper {

    @Override
    public DocumentMetadata mapToDocumentMetaData(UploadRequest uploadRequest, String storageKey) {
        return DocumentMetadata.builder()
                .applicationId(uploadRequest.getApplicationId())
                .documentType(uploadRequest.getDocumentType())
                .fileName(uploadRequest.getFileName())
                .fileSize(uploadRequest.getFileSize())
                .storageKey(storageKey)
                .status(DocumentStatus.PENDING)
                .build();
    }

    @Override
    public DocumentResponse mapToDocumentResponse(DocumentMetadata metadata, String presignedURL) {
        return DocumentResponse.builder()
                .fileName(metadata.getFileName())
                .uploadUrl(presignedURL)
                .createdAt(metadata.getCreatedAt())
                .build();
    }

    @Override
    public DocumentUploadResponse mapToDocumentUploadResponse(DocumentMetadata metadata, String presignedURL) {
        return DocumentUploadResponse.builder()
                .id(metadata.getId())
                .status(metadata.getStatus())
                .fileName(metadata.getFileName())
                .uploadUrl(presignedURL)
                .fileType(metadata.getContentType())
                .build();
    }


}
