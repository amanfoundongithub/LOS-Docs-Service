package com.loan_org.document_service.infrastructure.web.mapper.impl;

import com.loan_org.document_service.document.dto.DocumentResponse;
import com.loan_org.document_service.document.dto.UploadDocumentCommand;
import com.loan_org.document_service.document.model.DocumentMetadata;
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
}
