package com.loan_org.document_service.document.service;

import com.loan_org.document_service.document.dto.DocumentResponseEntity;
import com.loan_org.document_service.document.dto.UploadDocumentOutput;
import com.loan_org.document_service.document.dto.UploadDocumentCommand;

import java.util.List;

public interface DocumentService {
    UploadDocumentOutput         initializeUpload(UploadDocumentCommand request);
    void                         confirmUpload(String storageKey);
    List<DocumentResponseEntity> getDocumentsByApplication(String applicationId);
    String                 generateDownloadUrl(String id);
}