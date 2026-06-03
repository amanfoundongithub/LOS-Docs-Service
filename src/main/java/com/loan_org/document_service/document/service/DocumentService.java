package com.loan_org.document_service.document.service;

import com.loan_org.document_service.document.dto.UploadRequest;
import com.loan_org.document_service.document.dto.DocumentResponse;

import java.util.List;

public interface DocumentService {
    DocumentResponse       initializeUpload(UploadRequest request);
    DocumentResponse       confirmUpload(String id);
    List<DocumentResponse> getDocumentsByApplication(String applicationId);
    String                 generateDownloadUrl(String id);
}