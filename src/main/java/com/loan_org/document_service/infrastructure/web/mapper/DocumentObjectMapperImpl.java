package com.loan_org.document_service.infrastructure.web.mapper;

import com.loan_org.document_service.document.dto.DocumentResponseEntity;
import com.loan_org.document_service.document.dto.DownloadDocumentOutput;
import com.loan_org.document_service.infrastructure.web.dto.download.DocumentDownloadHttpResponse;
import com.loan_org.document_service.infrastructure.web.dto.fetch_by_application_id.DocumentResponseHttpEntity;
import com.loan_org.document_service.document.dto.upload.UploadDocumentCommand;
import com.loan_org.document_service.document.dto.upload.UploadDocumentOutput;
import com.loan_org.document_service.infrastructure.web.dto.upload.DocumentUploadHttpResponse;
import com.loan_org.document_service.infrastructure.web.dto.upload.DocumentUploadHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentObjectMapperImpl implements DocumentObjectMapper {

    @Override
    public UploadDocumentCommand toUploadCommand(DocumentUploadHttpRequest request) {
        if (request == null){
            return null;
        }
        return new UploadDocumentCommand(
                request.getApplicationId(),
                request.getCustomerId(),
                request.getDocumentType(),
                request.getContentType(),
                request.getFileName(),
                request.getFileSize()
        );
    }

    @Override
    public DocumentUploadHttpResponse toHttpUploadResponse(UploadDocumentOutput uploadResponse) {
        if(uploadResponse == null) {
            return null;
        }
        return DocumentUploadHttpResponse.builder()
                .id(uploadResponse.documentId())
                .fileName(uploadResponse.fileName())
                .fileType(uploadResponse.fileType())
                .contentType(uploadResponse.contentType())
                .status(uploadResponse.status())
                .uploadUrl(uploadResponse.uploadUrl())
                .build();
    }

    @Override
    public List<DocumentResponseHttpEntity> toDocumentResponsesHttpEntities(List<DocumentResponseEntity> entities) {
        return entities.stream()
                .map(this::toDocResponse)
                .toList();
    }

    @Override
    public DocumentDownloadHttpResponse toDocumentDownloadHttpResponse(DownloadDocumentOutput output) {
        return DocumentDownloadHttpResponse.builder()
                .downloadUrl(output.downloadUrl())
                .validForMinutes(output.validForMinutes())
                .createdTimestamp(output.createdTimestamp())
                .build();
    }

    private DocumentResponseHttpEntity toDocResponse(DocumentResponseEntity entity) {
        return DocumentResponseHttpEntity.builder()
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
