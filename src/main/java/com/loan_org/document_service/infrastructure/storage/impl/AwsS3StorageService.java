package com.loan_org.document_service.infrastructure.storage.impl;

import com.loan_org.document_service.infrastructure.storage.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@Primary
public class AwsS3StorageService implements StorageService {

    private final S3Presigner s3Presigner;
    private final String bucketName;

    public AwsS3StorageService(S3Presigner s3Presigner,
                               @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
    }

    @Override
    public String generatePresignedUploadUrl(String storageKey, Duration duration) {
        // 1. Describe the S3 object metadata targeting our secured bucket
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .build();

        // 2. Configure the signing operation details (including time-to-live)
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(duration)
                .putObjectRequest(putObjectRequest)
                .build();

        // 3. Perform the cryptographic signing and extract the fully qualified URL
        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }

    @Override
    public String generatePresignedDownloadUrl(String storageKey, Duration duration) {
        // 1. Define the object path target within our private bucket
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .build();

        // 2. Set the read signature constraints (e.g., valid for 10 minutes)
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(duration)
                .getObjectRequest(getObjectRequest)
                .build();

        // 3. Graphically sign the read request and hand back the temporary string
        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}