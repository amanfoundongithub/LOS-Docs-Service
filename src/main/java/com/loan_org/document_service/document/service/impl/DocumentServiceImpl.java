package com.loan_org.document_service.document.service.impl;

import com.loan_org.document_service.document.dto.UploadRequest;
import com.loan_org.document_service.document.dto.DocumentResponse;
import com.loan_org.document_service.document.model.DocumentMetadata;
import com.loan_org.document_service.document.model.DocumentStatus;
import com.loan_org.document_service.document.repository.DocumentRepository;
import com.loan_org.document_service.document.service.DocumentService;
import com.loan_org.document_service.exception.classes.DocumentNotFoundException;
import com.loan_org.document_service.infrastructure.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor // Automatically constructor-injects fields marked 'final'
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final StorageService storageService;

    @Override
    @Transactional
    public DocumentResponse initializeUpload(UploadRequest request) {
        log.info("Initializing document upload for application: {}, type: {}",
                request.getApplicationId(), request.getDocumentType());

        // 1. Generate a predictable, clean storage path for the cloud bucket
        String uniqueFileId = UUID.randomUUID().toString();
        String storageKey = String.format("loans/%s/%s-%s",
                request.getApplicationId(), uniqueFileId, request.getFileName());

        // 2. Request a secure upload link from our storage provider (valid for 15 minutes)
        String presignedUrl = storageService.generatePresignedUploadUrl(storageKey, Duration.ofMinutes(15));

        // 3. Map the DTO to our MongoDB Entity
        DocumentMetadata metadata = DocumentMetadata.builder()
                .applicationId(request.getApplicationId())
                .documentType(request.getDocumentType())
                .fileName(request.getFileName())
                .fileSize(request.getFileSize())
                .storageKey(storageKey)
                .status(DocumentStatus.PENDING) // Explicitly starting as PENDING
                .build();

        // 4. Persist metadata to MongoDB
        DocumentMetadata savedMetadata = documentRepository.save(metadata);
        log.info("Document metadata record saved successfully with ID: {}", savedMetadata.getId());

        // 5. Map the saved entity back to our sanitized public Response DTO
        return DocumentResponse.builder()
                .id(savedMetadata.getId())
                .applicationId(savedMetadata.getApplicationId())
                .documentType(savedMetadata.getDocumentType())
                .fileName(savedMetadata.getFileName())
                .fileSize(savedMetadata.getFileSize())
                .status(savedMetadata.getStatus())
                .uploadUrl(presignedUrl)
                .createdAt(savedMetadata.getCreatedAt())
                .updatedAt(savedMetadata.getUpdatedAt())
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
        // eventPublisher.publish(new DocumentUploadedEvent(updatedMetadata.getId()));

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
        return storageService.generatePresignedDownloadUrl(metadata.getStorageKey(), Duration.ofMinutes(10));
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