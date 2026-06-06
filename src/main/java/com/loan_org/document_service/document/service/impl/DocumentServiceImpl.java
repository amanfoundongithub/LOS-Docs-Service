package com.loan_org.document_service.document.service.impl;

import com.loan_org.document_service.document.dto.UploadDocumentOutput;
import com.loan_org.document_service.document.dto.UploadDocumentCommand;
import com.loan_org.document_service.document.dto.DocumentResponse;
import com.loan_org.document_service.document.model.DocumentMetadata;
import com.loan_org.document_service.document.model.DocumentStatus;
import com.loan_org.document_service.document.port.DocumentRepository;
import com.loan_org.document_service.document.port.DocumentStorageService;
import com.loan_org.document_service.document.service.DocumentService;
import com.loan_org.document_service.exception.classes.DocumentNotFoundException;
import com.loan_org.document_service.document.port.StorageKeyResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .id(metadata.getId())
                .status(metadata.getStatus())
                .fileName(metadata.getFileName())
                .uploadUrl(presignedUrl)
                .fileType(metadata.getContentType())
                .build();
    }

    @Override
    @Transactional
    public DocumentResponse confirmUpload(String id) {
        log.info("Confirming upload completion for document ID: {}", id);

        // 1. Fetch record or throw our custom 404 exception if it's missing
        DocumentMetadata metadata = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with ID: " + id));

        // 2. State-Guard: Ensure we can only confirm documents that are actually PENDING
        if (metadata.getStatus() != DocumentStatus.PENDING) {
            log.warn("Invalid state transition attempt for document ID: {}. Current state: {}", id, metadata.getStatus());
            throw new IllegalStateException("Document upload cannot be confirmed because status is: " + metadata.getStatus());
        }

        // 3. Mutate status to UPLOADED
        metadata.setStatus(DocumentStatus.UPLOADED);

        // 4. Persist (Optimistic Locking via @Version handles concurrent conflicts here seamlessly!)
        DocumentMetadata updatedMetadata = documentRepository.save(metadata);
        log.info("Document ID: {} successfully updated to status: UPLOADED", id);

        // TODO: In the next phases, we would emit a Kafka/RabbitMQ event right here!

        return mapToResponse(updatedMetadata);
    }

    @Override
    @Transactional(readOnly = true) // Production Upgrade: Optimizes database resource allocation for reads
    public List<DocumentResponse> getDocumentsByApplication(String applicationId) {
        log.info("Fetching all documents associated with application ID: {}", applicationId);

        // 1. Fetch the documents from the database (utilizing the index we built on applicationId)
        List<DocumentMetadata> documents = documentRepository.findByApplicationId(applicationId);

        // 2. Stream through the results, map each entity to our clean Response DTO, and compile them into a list
        return documents.stream()
                .map(this::mapToResponse)
                .toList(); // Java 17 compact syntax
    }

    @Override
    @Transactional(readOnly = true) // Read-only optimization
    public String generateDownloadUrl(String id) {
        log.info("Generating secure download URL for document ID: {}", id);

        // 1. Fetch metadata record from MongoDB
        DocumentMetadata metadata = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with ID: " + id));

        // 2. State-Guard: Block link generation if the file bytes aren't verified yet
        if (metadata.getStatus() != DocumentStatus.UPLOADED) {
            log.warn("Unauthorized download URL request for un-uploaded document ID: {}. Current status: {}", id, metadata.getStatus());
            throw new IllegalStateException("Cannot generate download link because document status is: " + metadata.getStatus());
        }

        // 3. Generate a 10-minute read URL using the saved storage key path
        return storageService.generateDownloadURL(metadata.getStorageKey());
    }

    private DocumentResponse mapToResponse(DocumentMetadata metadata) {
        return DocumentResponse.builder()
                .id(metadata.getId())
                .applicationId(metadata.getApplicationId())
                .documentType(metadata.getDocumentType())
                .fileName(metadata.getFileName())
                .fileSize(metadata.getFileSize())
                .status(metadata.getStatus())
                .createdAt(metadata.getCreatedAt())
                .updatedAt(metadata.getUpdatedAt())
                .build();
    }
}