package com.loan_org.document_service.infrastructure.web.mapper.impl;

import com.loan_org.document_service.document.dto.DocumentResponse;
import com.loan_org.document_service.document.dto.UploadDocumentCommand;
import com.loan_org.document_service.document.dto.UploadDocumentResponse;
import com.loan_org.document_service.document.model.DocumentMetadata;
import com.loan_org.document_service.infrastructure.web.dto.DocumentUploadResponse;
import com.loan_org.document_service.infrastructure.web.dto.UploadRequest;
import com.loan_org.document_service.infrastructure.web.mapper.DocumentObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class DocumentObjectMapperImpl implements DocumentObjectMapper {

    @Override
    public UploadDocumentCommand toCommand(UploadRequest request) {
        if (request == null){
            return null;
        }
        return new UploadDocumentCommand(
                request.getApplicationId(),
                request.getDocumentType(),
                request.getFileName(),
                request.getFileSize()
        );
    }

    @Override
    public DocumentResponse toResponse(DocumentMetadata metadata, String uploadUrl) {
        if (metadata == null){
            return null;
        }
        return DocumentResponse.builder()
                .id(metadata.getId())
                .fileName(metadata.getFileName())
                .status(metadata.getStatus())
                .uploadUrl(uploadUrl)
                .build();
    }

    @Override
    public DocumentUploadResponse toControllerResponse(UploadDocumentResponse uploadResponse) {
        if(uploadResponse == null) {
            return null;
        }
        return DocumentUploadResponse.builder()
                .id(uploadResponse.id())
                .fileName(uploadResponse.fileName())
                .fileType(uploadResponse.fileType())
                .status(uploadResponse.status().name())
                .uploadUrl(uploadResponse.uploadUrl())
                .build();
    }


}
