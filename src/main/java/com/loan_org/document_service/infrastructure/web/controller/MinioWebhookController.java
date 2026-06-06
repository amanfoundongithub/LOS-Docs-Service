package com.loan_org.document_service.infrastructure.web.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loan_org.document_service.document.service.DocumentService;
import com.loan_org.document_service.infrastructure.web.exception.classes.PermissionDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/v1/internal")
@RequiredArgsConstructor
@Slf4j
public class MinioWebhookController {

    @Value("${minio.webhook.secret-token}")
    private String expectedSecretToken;

    private final DocumentService documentService;

    @PostMapping("/minio-callback")
    public ResponseEntity<Void> handleMinioNotification(
            @RequestHeader(value = "Authorization", required = false) String authToken,
            @RequestBody MinioWebhookPayload payload) {

        if (authToken == null || !authToken.contains(expectedSecretToken)) {
            throw new PermissionDeniedException("/api/v1/internal/minio-callback", "Unauthorized entry into the MinIO callback");
        }

        if (payload.records() == null || payload.records().isEmpty()) {
            log.info("MinIO ping received and verified successfully!");
            return ResponseEntity.ok().build();
        }

        for (MinioRecord record : payload.records()) {
            String rawKey  = record.s3().object().key();
            String fileKey = URLDecoder.decode(rawKey, StandardCharsets.UTF_8);
            long fileSize  = record.s3().object().size();

            log.info("Received file with key: {} & size: {}. Persisting to database...", fileKey, fileSize);
            documentService.confirmUpload(fileKey);
        }

        return ResponseEntity.ok().build();
    }
}

// --- DTO Records for Jackson JSON Parsing ---

record MinioWebhookPayload(
        @JsonProperty("Records") List<MinioRecord> records
) {}

record MinioRecord(
        S3Data s3
) {}

record S3Data(
        BucketData bucket,
        ObjectData object
) {}

record BucketData(
        String name
) {}

record ObjectData(
        String key,
        long size
) {}