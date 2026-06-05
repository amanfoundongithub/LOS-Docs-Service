package com.loan_org.document_service.infrastructure.storage.impl;

import com.loan_org.document_service.document.dto.UploadRequest;
import com.loan_org.document_service.infrastructure.storage.FileStorageNamingHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FileStorageNameHelperUsingUUIDImpl implements FileStorageNamingHelper {

    @Override
    public String createStorageKey(UploadRequest uploadRequest) {
        String uniqueFileId = UUID.randomUUID().toString();
        String sanitizedFileName = uploadRequest.getFileName().replaceAll("\\s+", "_");
        return String.format("loans/%s/%s-%s",
                uploadRequest.getApplicationId(),
                uniqueFileId,
                sanitizedFileName
        );
    }
}
