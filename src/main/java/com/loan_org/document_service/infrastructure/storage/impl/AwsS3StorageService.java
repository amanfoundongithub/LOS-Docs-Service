package com.loan_org.document_service.infrastructure.storage.impl;

import com.loan_org.document_service.document.exception.StorageStreamException;
import com.loan_org.document_service.document.port.DocumentStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.InputStream;
import java.time.Duration;

@Service
@Primary
public class AwsS3StorageService implements DocumentStorageService {

    @Value("${minio.upload.validity_in_minutes}")
    private int validityInMinutes;

    @Value("${minio.download.validity_in_minutes}")
    private int downloadValidityInMinutes;

    private final S3Presigner s3Presigner;
    private final S3Client    s3Client;
    private final String      bucketName;

    public AwsS3StorageService(S3Presigner s3Presigner,
                               S3Client s3Client,
                               @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Presigner = s3Presigner;
        this.bucketName  = bucketName;
        this.s3Client    = s3Client;
    }

    @Override
    public String generateUploadURL(String storageKey) {

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(validityInMinutes))
                .putObjectRequest(req -> req.bucket(bucketName).key(storageKey))
                .build();

        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }

    @Override
    public String generateDownloadURL(String storageKey) {

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(downloadValidityInMinutes))
                .getObjectRequest(req -> req.bucket(bucketName).key(storageKey))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    @Override
    public int getUploadDocumentValidityInMinutes() {
        return validityInMinutes;
    }

    @Override
    public int getDownloadDocumentValidityInMinutes() {
        return downloadValidityInMinutes;
    }

    @Override
    public InputStream downloadStream(String storageKey) {

        try {
            return s3Client.getObject(req -> req.bucket(bucketName).key(storageKey));
        } catch (Exception e) {
            throw new StorageStreamException("Failed to stream target file from storage cluster vendor.", e);
        }

    }

}