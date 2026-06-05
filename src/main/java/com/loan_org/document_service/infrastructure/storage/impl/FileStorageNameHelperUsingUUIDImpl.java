package com.loan_org.document_service.infrastructure.storage.impl;

import com.loan_org.document_service.document.dto.UploadDocumentCommand;
import com.loan_org.document_service.document.port.StorageKeyResolver;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FileStorageNameHelperUsingUUIDImpl implements StorageKeyResolver {

    @Override
    public String createStorageKey(UploadDocumentCommand uploadRequest) {
        String uniqueFileId = UUID.randomUUID().toString();
        String sanitizedFileName = uploadRequest.fileName().replaceAll("\\s+", "_");
        return String.format("loans/%s/%s-%s",
                uploadRequest.applicationId(),
                uniqueFileId,
                sanitizedFileName
        );
    }
}
