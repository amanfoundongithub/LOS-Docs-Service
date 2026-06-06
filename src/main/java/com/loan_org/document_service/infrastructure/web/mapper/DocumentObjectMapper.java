package com.loan_org.document_service.infrastructure.web.mapper;

import com.loan_org.document_service.document.dto.DocumentResponseEntity;
import com.loan_org.document_service.infrastructure.web.dto.DocumentResponse;
import com.loan_org.document_service.document.dto.UploadDocumentCommand;
import com.loan_org.document_service.document.dto.UploadDocumentOutput;
import com.loan_org.document_service.document.model.DocumentMetadata;
import com.loan_org.document_service.infrastructure.web.dto.DocumentUploadResponse;
import com.loan_org.document_service.infrastructure.web.dto.UploadRequest;

import java.util.List;

public interface DocumentObjectMapper {
    UploadDocumentCommand toCommand(UploadRequest request);
    DocumentUploadResponse toControllerResponse(UploadDocumentOutput uploadResponse);

    List<DocumentResponse> toDocumentResponses(List<DocumentResponseEntity> entities);

}
