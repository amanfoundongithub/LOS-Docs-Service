package com.loan_org.document_service.document.service;

import com.loan_org.document_service.document.dto.DocumentResponseEntity;
import com.loan_org.document_service.document.dto.DocumentUpdateResponse;
import com.loan_org.document_service.document.dto.DownloadDocumentOutput;
import com.loan_org.document_service.document.dto.upload.UploadDocumentOutput;
import com.loan_org.document_service.document.dto.upload.UploadDocumentCommand;
import com.loan_org.document_service.document.exception.IllegalStateTransitionException;
import com.loan_org.document_service.document.model.AllowedContentType;
import com.loan_org.document_service.document.model.DocumentMetadata;
import com.loan_org.document_service.document.model.DocumentStatus;
import com.loan_org.document_service.document.port.DocumentRepository;
import com.loan_org.document_service.document.port.DocumentScanner;
import com.loan_org.document_service.document.port.DocumentStorageService;
import com.loan_org.document_service.document.exception.DocumentNotFoundException;
import com.loan_org.document_service.document.port.StorageKeyResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    // Services to inject for help
    private final DocumentRepository      documentRepository;
    private final DocumentStorageService  storageService;
    private final StorageKeyResolver      namingHelper;
    private final DocumentScanner         documentScanner;

    // Downloadable options
    private final List<DocumentStatus> documentAvailableForDownload = List.of(
            DocumentStatus.AVAILABLE,
            DocumentStatus.UPLOADED,
            DocumentStatus.PROCESSING
    );

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
        int    validity     = storageService.getUploadDocumentValidityInMinutes();
        log.info("[DOCUMENT_SERVICE][START] Successful generation of MinIO URL for uploading document. Validity: {} minutes.",
                validity);

        // Unpack the request, as a metadata and persist it
        DocumentMetadata metadata = DocumentMetadata.builder()
                .applicationId(request.applicationId())
                .documentType(request.documentType())
                .contentType(AllowedContentType.fromMimeType(request.contentType()))
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
                .documentId(savedMetadata.getId())
                .status(savedMetadata.getStatus())
                .fileName(savedMetadata.getFileName())
                .uploadUrl(presignedUrl)
                .fileType(String.valueOf(savedMetadata.getDocumentType()))
                .build();
    }

    @Override
    @Transactional
    public void confirmUpload(String storageKey) {

        // Log the acknowledgement that we received the document
        log.info("[DOCUMENT_SERVICE][CONFIRM] Received document for storageKey: {} from user.", storageKey);

        // Search in MongoDB, or else throw exception
        DocumentMetadata metadata = documentRepository.findByStorageKey(storageKey)
                .orElseThrow(() -> new DocumentNotFoundException("storageKey", storageKey));

        log.info("[DOCUMENT_SERVICE][CONFIRM] Fetched document metadata successfully. Now confirming the upload...");

        // Mutate status to UPLOADED
        metadata.setStatus(DocumentStatus.UPLOADED);
        DocumentMetadata updatedMetadata = documentRepository.save(metadata);
        log.info("[DOCUMENT_SERVICE][CONFIRM] The document in the storageKey: {} has been successfully confirmed! Updated status to : {}. Starting background scans now...",
                storageKey,
                updatedMetadata.getStatus().name());

        // Start background scanning for confirmation
        documentScanner.startScan(updatedMetadata);

    }

    @Override
    @Transactional(readOnly = true)
    public DocumentResponseEntity getDocumentByStorageKey(String storageKey) {

        // Log the acknowledgment
        log.info("[DOCUMENT_SERVICE][FETCH_ONE] Fetching document for storageKey: {}",
                storageKey);

        // Fetch document
        return mapToResponseEntity(
                documentRepository.findByStorageKey(storageKey)
                        .orElseThrow(() -> new DocumentNotFoundException("storageKey", storageKey))
        );
    }

    @Override
    @Transactional
    public DocumentUpdateResponse updateDocument(String storageKey) {

        // Log the acknowledgment for update
        log.info("[DOCUMENT_SERVICE][UPDATE] Received the request to update document for storageKey: {}",
                storageKey);

        String updateUrl = storageService.generateUpdateURL(storageKey);
        log.info("[DOCUMENT_SERVICE][UPDATE] Generated an update URL for: {}. Now sending this to user now...",
                storageKey);

        return new DocumentUpdateResponse(updateUrl);
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
        log.info("[DOCUMENT_SERVICE][DOWNLOAD] Generating a secure download URL for storageKey: {}...", storageKey);

        // Fetch metadata record from MongoDB
        DocumentMetadata metadata = documentRepository.findByStorageKey(storageKey)
                .orElseThrow(() -> new DocumentNotFoundException("storageKey", storageKey));
        log.info("[DOCUMENT_SERVICE][DOWNLOAD] Found the record in database. Checking if document is uploaded or not...");

        // State-Guard: Block link generation if the file bytes aren't verified yet
        if (!documentAvailableForDownload.contains(metadata.getStatus())) {
            log.warn("[DOCUMENT_SERVICE][DOWNLOAD] Cannot generate URL for {} as document is currently: {}", storageKey, metadata.getStatus());
            throw new IllegalStateTransitionException("Cannot generate download link because document status is: " + metadata.getStatus(), "abed");
        }

        // Generate a download URL
        String url   = storageService.generateDownloadURL(storageKey);
        int validity = storageService.getDownloadDocumentValidityInMinutes();

        log.info("[DOCUMENT_SERVICE][DOWNLOAD] Generated download URL for {} (validity : {} minutes). Sending to user...",
                storageKey,
                validity);

        return DownloadDocumentOutput.builder()
                .downloadUrl(url)
                .createdTimestamp(Instant.now())
                .validForMinutes(validity)
                .build();
    }

    @Override
    @Transactional
    public void deleteDocument(String storageKey) {

        // Log the acknowledgment for the deletion request
        log.info("[DOCUMENT_SERVICE][DELETE] Received request for deleting document in storageKey: {}",
                storageKey);

        DocumentMetadata documentMetadata = documentRepository.findByStorageKey(storageKey)
                .orElseThrow(() -> new DocumentNotFoundException("storageKey", storageKey));
        log.info("[DOCUMENT_SERVICE][DELETE] Found record in database. Deleting the record now...");

        // Soft-delete the document (mark as archived)
        documentMetadata.setStatus(DocumentStatus.ARCHIVED);
        documentRepository.save(documentMetadata);
        log.info("[DOCUMENT_SERVICE][DELETE] Deleted document in storageKey: {} successfully.",
                storageKey);
    }


    private DocumentResponseEntity mapToResponseEntity(DocumentMetadata metadata) {
        return DocumentResponseEntity.builder()
                .documentType(String.valueOf(metadata.getDocumentType()))
                .fileName(metadata.getFileName())
                .fileSize(metadata.getFileSize())
                .status(metadata.getStatus())
                .createdAt(metadata.getCreatedAt())
                .updatedAt(metadata.getUpdatedAt())
                .storageKey(metadata.getStorageKey())
                .build();
    }
}