package com.loan_org.document_service.document.service;

import com.loan_org.document_service.document.dto.fetch.DocumentResponseEntity;
import com.loan_org.document_service.document.dto.update.DocumentUpdateResponse;
import com.loan_org.document_service.document.dto.download.DownloadDocumentOutput;
import com.loan_org.document_service.document.dto.upload.UploadDocumentOutput;
import com.loan_org.document_service.document.dto.upload.UploadDocumentCommand;

import java.util.List;

public interface DocumentService {
    UploadDocumentOutput         initializeUpload(UploadDocumentCommand request);
    void                         confirmUpload(String storageKey);
    DocumentResponseEntity       getDocumentByStorageKey(String storageKey);
    DocumentUpdateResponse       updateDocument(String storageKey);
    List<DocumentResponseEntity> getDocumentsByApplication(String applicationId);
    DownloadDocumentOutput       generateDownloadUrl(String storageKey);
    void                         deleteDocument(String storageKey);
}