package com.loan_org.document_service.document.service;

import com.loan_org.document_service.document.dto.DocumentResponseEntity;
import com.loan_org.document_service.document.dto.DownloadDocumentOutput;
import com.loan_org.document_service.document.dto.upload.UploadDocumentOutput;
import com.loan_org.document_service.document.dto.upload.UploadDocumentCommand;

import java.util.List;

public interface DocumentService {
    UploadDocumentOutput         initializeUpload(UploadDocumentCommand request);
    void                         confirmUpload(String storageKey);
    List<DocumentResponseEntity> getDocumentsByApplication(String applicationId);
    DownloadDocumentOutput       generateDownloadUrl(String storageKey);
    void                         deleteDocument(String storageKey);
}