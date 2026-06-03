package com.loan_org.document_service.infrastructure.storage;

import java.time.Duration;

public interface StorageService {
    String generatePresignedUploadUrl(String storageKey, Duration duration);
    String generatePresignedDownloadUrl(String storageKey, Duration duration);
}