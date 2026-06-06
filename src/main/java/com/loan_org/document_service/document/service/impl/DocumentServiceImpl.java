package com.loan_org.document_service.document.service.impl;

import com.loan_org.document_service.document.dto.DocumentResponseEntity;
import com.loan_org.document_service.document.dto.DownloadDocumentOutput;
import com.loan_org.document_service.document.dto.UploadDocumentOutput;
import com.loan_org.document_service.document.dto.UploadDocumentCommand;
import com.loan_org.document_service.document.exception.IllegalStateTransitionException;
import com.loan_org.document_service.document.model.DocumentMetadata;
import com.loan_org.document_service.document.model.DocumentStatus;
import com.loan_org.document_service.document.port.DocumentRepository;
import com.loan_org.document_service.document.port.DocumentStorageService;
import com.loan_org.document_service.document.service.DocumentService;
import com.loan_org.document_service.document.exception.DocumentNotFoundException;
import com.loan_org.document_service.document.port.StorageKeyResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    // Services to inject for help
    private final DocumentRepository      documentRepository;
    private final DocumentStorageService  storageService;
    private final StorageKeyResolver      namingHelper;

    @Override
    @Transactional
    public UploadDocumentOutput initializeUpload(UploadDocumentCommand request) {

        // Log the request received acknowledgment
        log.info("[DOCUMENT_SERVICE][START] Received request to upload document {} for applicationId: {}. Starting upload now...",
                request.fileName(), request.applicationId());

        // Generate a storage key for AWS
        String storageKey = namingHelper.createStorageKey(request);
        log.info("[DOCUMENT_SERVICE][START] Generated the storage key: {}. Generating URL now...",
                storageKey);

        // Generate a presigned URL for uploading document
        String presignedUrl = storageService.generateUploadURL(storageKey);
        log.info("[DOCUMENT_SERVICE][START] Successful generation of MinIO URL");

        // Unpack the request, as a metadata and persist it
        DocumentMetadata metadata = DocumentMetadata.builder()
                .applicationId(request.applicationId())
                .documentType(request.documentType())
                .fileName(request.fileName())
                .fileSize(request.fileSize())
                .storageKey(storageKey)
                .status(DocumentStatus.PENDING)
                .build();
        DocumentMetadata savedMetadata = documentRepository.save(metadata);

        log.info("[DOCUMENT_SERVICE][START] Successfully persisted the data for the document with generated MongoID: {}. Sending URL to user for uploading...",
                savedMetadata.getId());

        // Return the object back to the user
        return UploadDocumentOutput.builder()
                .id(savedMetadata.getId())
                .status(savedMetadata.getStatus())
                .fileName(savedMetadata.getFileName())
                .uploadUrl(presignedUrl)
                .fileType(savedMetadata.getDocumentType())
                .build();
    }

    @Override
    @Transactional
    public void confirmUpload(String storageKey) {

        // Log the acknowledgement that we received the document
        log.info("[DOCUMENT_SERVICE][CONFIRM] Received document for storageKey: {}", storageKey);

        // Search in MongoDB, or else throw exception
        DocumentMetadata metadata = documentRepository.findByStorageKey(storageKey)
                .orElseThrow(() -> new DocumentNotFoundException("storageKey", storageKey));

        log.info("[DOCUMENT_SERVICE][CONFIRM] Fetched document successfully. Now confirming the upload...");

        // If it is not pending, then why are we even doing this?
        if (metadata.getStatus() != DocumentStatus.PENDING) {
            log.warn("[DOCUMENT_SERVICE][CONFIRM] Invalid state transition attempted for storageKey: {}. Current state: {}", storageKey, metadata.getStatus());
            throw new IllegalStateTransitionException("Document upload cannot be confirmed because status is: " + metadata.getStatus(), "/api/v1");
        }

        // Mutate status to UPLOADED
        metadata.setStatus(DocumentStatus.UPLOADED);
        DocumentMetadata updatedMetadata = documentRepository.save(metadata);
        log.info("[DOCUMENT_SERVICE][CONFIRM] The document in the storageKey: {} has been successfully confirmed! Updated status to : {}. Persisting data now...",
                storageKey,
                updatedMetadata.getStatus().name());

        // TODO: Emit Kafka event here, will do this later here...
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponseEntity> getDocumentsByApplication(String applicationId) {

        // Log the request that we acknowledged the request
        log.info("[DOCUMENT_SERVICE][FETCH_ALL] Received request to fetch all documents for the applicationId : {}. Starting fetching now...",
                applicationId);

        // Fetch the documents from the database
        List<DocumentMetadata> documents = documentRepository.findByApplicationId(applicationId);
        log.info("[DOCUMENT_SERVICE][FETCH_ALL] Successfully fetched {} records from the database for applicationId: {}. Sending them to user now...",
                documents.size(),
                applicationId);

        // Stream through the results, map each entity to our cleaned entity
        return documents.stream()
                .map(this::mapToResponseEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DownloadDocumentOutput generateDownloadUrl(String storageKey) {

        // Log the acknowledgment that we are generating URL
        log.info("[DOCUMENT_SERVICE][DOWNLOAD] Generating a secure download URL for storageKey: {}", storageKey);

        // Fetch metadata record from MongoDB
        DocumentMetadata metadata = documentRepository.findByStorageKey(storageKey)
                .orElseThrow(() -> new DocumentNotFoundException("storageKey", storageKey));
        log.info("[DOCUMENT_SERVICE][DOWNLOAD] Found the record in database. Checking if document uploaded or not...");

        // State-Guard: Block link generation if the file bytes aren't verified yet
        if (metadata.getStatus() != DocumentStatus.UPLOADED) {
            log.warn("[DOCUMENT_SERVICE][DOWNLOAD] Cannot generate URL for {} as document is currently: {}", storageKey, metadata.getStatus());
            throw new IllegalStateTransitionException("Cannot generate download link because document status is: " + metadata.getStatus(), "abed");
        }

        // Generate a download URL
        String url = storageService.generateDownloadURL(storageKey);
        int validity = storageService.getValidityForMinutes();

        log.info("[DOCUMENT_SERVICE][DOWNLOAD] Generated download URL for {} (validity : {} minutes). Sending to user...",
                storageKey,
                validity);

        return DownloadDocumentOutput.builder()
                .downloadUrl(url)
                .createdTimestamp(Instant.now())
                .validForMinutes(validity)
                .build();
    }

    private DocumentResponseEntity mapToResponseEntity(DocumentMetadata metadata) {
        return DocumentResponseEntity.builder()
                .documentType(metadata.getDocumentType())
                .fileName(metadata.getFileName())
                .fileSize(metadata.getFileSize())
                .status(metadata.getStatus())
                .createdAt(metadata.getCreatedAt())
                .updatedAt(metadata.getUpdatedAt())
                .storageKey(metadata.getStorageKey())
                .build();
    }
}