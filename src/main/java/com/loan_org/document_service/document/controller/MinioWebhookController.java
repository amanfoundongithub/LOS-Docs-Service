package com.loan_org.document_service.document.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/v1/internal")
public class MinioWebhookController {

    @Value("${minio.webhook.secret-token}")
    private String expectedSecretToken;

    @PostMapping("/minio-callback")
    public ResponseEntity<Void> handleMinioNotification(
            @RequestHeader(value = "Authorization", required = false) String authToken,
            @RequestBody MinioWebhookPayload payload) {

        // 1. Security Check: Validate the auth_token
        if (authToken == null || !authToken.contains(expectedSecretToken)) {
            System.err.println("❌ Unauthorized webhook attempt blocked!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // MinIO sends an empty records array as a keep-alive test when you run 'mc admin config set'
        if (payload.records() == null || payload.records().isEmpty()) {
            System.out.println("🔄 MinIO ping received and verified successfully!");
            return ResponseEntity.ok().build();
        }

        try {
            // 2. Process the actual upload events
            for (MinioRecord record : payload.records()) {
                String bucketName = record.s3().bucket().name();
                String rawKey = record.s3().object().key();

                // Decode the object key (spaces become %20 or + in URLs)
                String fileKey = URLDecoder.decode(rawKey, StandardCharsets.UTF_8);
                long fileSize = record.s3().object().size();

                System.out.println("=========================================");
                System.out.println("🎉 SUCCESS: File Uploaded to MinIO!");
                System.out.printf("Bucket: %s%n", bucketName);
                System.out.printf("File Key: %s%n", fileKey);
                System.out.printf("Size: %d bytes%n", fileSize);
                System.out.println("=========================================");

                // TODO: Your DB logic goes here (e.g., documentRepository.updateStatus(fileKey, "UPLOADED"))
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            System.err.println("💥 Error processing webhook payload: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
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