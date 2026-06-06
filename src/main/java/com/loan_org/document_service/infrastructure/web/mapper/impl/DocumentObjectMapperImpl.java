package com.loan_org.document_service.infrastructure.web.mapper.impl;

import com.loan_org.document_service.document.dto.DocumentResponseEntity;
import com.loan_org.document_service.document.dto.DownloadDocumentOutput;
import com.loan_org.document_service.infrastructure.web.dto.DocumentDownloadResponse;
import com.loan_org.document_service.infrastructure.web.dto.DocumentResponse;
import com.loan_org.document_service.document.dto.UploadDocumentCommand;
import com.loan_org.document_service.document.dto.UploadDocumentOutput;
import com.loan_org.document_service.infrastructure.web.dto.DocumentUploadResponse;
import com.loan_org.document_service.infrastructure.web.dto.UploadRequest;
import com.loan_org.document_service.infrastructure.web.mapper.DocumentObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public DocumentUploadResponse toControllerResponse(UploadDocumentOutput uploadResponse) {
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

    @Override
    public List<DocumentResponse> toDocumentResponses(List<DocumentResponseEntity> entities) {
        return entities.stream()
                .map(this::toDocResponse)
                .toList();
    }

    @Override
    public DocumentDownloadResponse toDocumentDownloadResponse(DownloadDocumentOutput output) {
        return DocumentDownloadResponse.builder()
                .downloadUrl(output.downloadUrl())
                .validForMinutes(output.validForMinutes())
                .createdTimestamp(output.createdTimestamp())
                .build();
    }

    private DocumentResponse toDocResponse(DocumentResponseEntity entity) {
        return DocumentResponse.builder()
                .documentType(entity.documentType())
                .createdAt(entity.createdAt())
                .fileSize(entity.fileSize())
                .fileName(entity.fileName())
                .updatedAt(entity.updatedAt())
                .status(entity.status())
                .storageKey(entity.storageKey())
                .build();
    }

}
