package com.loan_org.document_service.infrastructure.storage.impl;

import com.loan_org.document_service.document.dto.upload.UploadDocumentCommand;
import com.loan_org.document_service.document.port.StorageKeyResolver;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidStorageKeyResolver implements StorageKeyResolver {

    @Override
    public String createStorageKey(UploadDocumentCommand command) {
        String uniqueFileId = UUID.randomUUID().toString();
        String sanitizedFileName = command.fileName()
                .toLowerCase()
                .replaceAll("\\s+", "_")
                .replaceAll("[^a-zA-Z0-9._-]", "");

        return String.format("loans/%s/%s-%s",
                command.applicationId(),
                uniqueFileId,
                sanitizedFileName
        );
    }
}
