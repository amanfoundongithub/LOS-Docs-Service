package com.loan_org.document_service.document.service;

import com.loan_org.document_service.document.dto.UploadDocumentOutput;
import com.loan_org.document_service.document.dto.UploadDocumentCommand;
import com.loan_org.document_service.document.dto.DocumentResponse;

import java.util.List;

public interface DocumentService {
    UploadDocumentOutput   initializeUpload(UploadDocumentCommand request);
    DocumentResponse       confirmUpload(String id);
    List<DocumentResponse> getDocumentsByApplication(String applicationId);
    String                 generateDownloadUrl(String id);
}