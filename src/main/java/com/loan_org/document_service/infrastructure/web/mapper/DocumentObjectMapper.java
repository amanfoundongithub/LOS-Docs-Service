package com.loan_org.document_service.infrastructure.web.mapper;

import com.loan_org.document_service.document.dto.DocumentResponseEntity;
import com.loan_org.document_service.document.dto.DocumentUpdateResponse;
import com.loan_org.document_service.document.dto.DownloadDocumentOutput;
import com.loan_org.document_service.infrastructure.web.dto.download.DocumentDownloadHttpResponse;
import com.loan_org.document_service.infrastructure.web.dto.fetch_by_application_id.DocumentResponseHttpEntity;
import com.loan_org.document_service.document.dto.upload.UploadDocumentCommand;
import com.loan_org.document_service.document.dto.upload.UploadDocumentOutput;
import com.loan_org.document_service.infrastructure.web.dto.update.DocumentUpdateHttpResponse;
import com.loan_org.document_service.infrastructure.web.dto.upload.DocumentUploadHttpResponse;
import com.loan_org.document_service.infrastructure.web.dto.upload.DocumentUploadHttpRequest;

import java.util.List;

public interface DocumentObjectMapper {

    // Upload Helpers
    UploadDocumentCommand            toUploadCommand(DocumentUploadHttpRequest request);
    DocumentUploadHttpResponse       toHttpUploadResponse(UploadDocumentOutput uploadResponse);

    // Fetching by Application ID
    List<DocumentResponseHttpEntity> toDocumentResponsesHttpEntities(List<DocumentResponseEntity> entities);
    DocumentResponseHttpEntity       toDocumentResponsesHttpEntity(DocumentResponseEntity entity);

    // Download helpers
    DocumentDownloadHttpResponse     toDocumentDownloadHttpResponse(DownloadDocumentOutput output);

    DocumentUpdateHttpResponse       toDocumentUpdateHttpResponse(DocumentUpdateResponse response);

}
