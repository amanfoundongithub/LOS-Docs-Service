package com.loan_org.document_service.infrastructure.storage.impl;

import com.loan_org.document_service.infrastructure.storage.StorageService;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class MockStorageService implements StorageService {

    @Override
    public String generatePresignedUploadUrl(String storageKey, Duration duration) {
        return "https://mock-s3-bucket.s3.amazonaws.com/" + storageKey + "?token=mock-crypto-handshake-sig";
    }

    @Override
    public String generatePresignedDownloadUrl(String storageKey, Duration duration) {
        return "";
    }

}