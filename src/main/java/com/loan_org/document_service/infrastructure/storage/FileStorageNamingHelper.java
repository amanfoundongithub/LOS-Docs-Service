package com.loan_org.document_service.infrastructure.storage;

import com.loan_org.document_service.document.dto.UploadRequest;

public interface FileStorageNamingHelper {
    String createStorageKey(UploadRequest uploadRequest);
}