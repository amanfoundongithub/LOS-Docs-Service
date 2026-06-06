package com.loan_org.document_service.infrastructure.storage.impl;

import com.loan_org.document_service.document.port.DocumentStorageService;
import org.springframework.stereotype.Service;

@Service
public class MockStorageService implements DocumentStorageService {

    @Override
    public String generateUploadURL(String storageKey) {
        return "https://mock-s3-bucket.s3.amazonaws.com/" + storageKey + "?token=mock-crypto-handshake-sig";
    }

    @Override
    public String generateDownloadURL(String storageKey) {
        return "";
    }

}