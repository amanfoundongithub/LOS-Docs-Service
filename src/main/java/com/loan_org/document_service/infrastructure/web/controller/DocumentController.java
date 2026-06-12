package com.loan_org.document_service.infrastructure.web.controller;

import com.loan_org.document_service.document.service.DocumentService;
import com.loan_org.document_service.infrastructure.web.dto.download.DocumentDownloadHttpRequest;
import com.loan_org.document_service.infrastructure.web.dto.download.DocumentDownloadHttpResponse;
import com.loan_org.document_service.infrastructure.web.dto.fetch_by_application_id.DocumentResponseHttpEntity;
import com.loan_org.document_service.infrastructure.web.dto.upload.DocumentUploadHttpRequest;
import com.loan_org.document_service.infrastructure.web.dto.upload.DocumentUploadHttpResponse;
import com.loan_org.document_service.infrastructure.web.guard.PermissionGuard;
import com.loan_org.document_service.infrastructure.web.mapper.DocumentObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    // Inject the service
    private final DocumentService      documentService;
    private final DocumentObjectMapper documentMapper;
    private final PermissionGuard      permissionGuard;

    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadHttpResponse> initializeUpload(@Valid @RequestBody DocumentUploadHttpRequest request,
                                                                       @RequestAttribute("attributes") Map<String, Object> attributes) {

        // Enforce guard for permission to user for uploading
        permissionGuard.canUserUpload(attributes, "/api/v1/documents/upload");

        // If permission is cleared, then start upload
        DocumentUploadHttpResponse response = documentMapper.toHttpUploadResponse(
                documentService.initializeUpload(
                        documentMapper.toUploadCommand(request)
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<List<DocumentResponseHttpEntity>> getDocumentsByApplication(@PathVariable String applicationId,
                                                                                      @RequestAttribute("attributes") Map<String, Object> attributes){

        // Enforce guard for permission to user for view
        permissionGuard.canUserView(attributes, "/api/v1/documents/applications/" + applicationId);

        // If permission is cleared, fetch all documents
        List<DocumentResponseHttpEntity> responseList = documentMapper.toDocumentResponsesHttpEntities(
                documentService.getDocumentsByApplication(applicationId)
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PostMapping("/download")
    public ResponseEntity<DocumentDownloadHttpResponse> getDownloadUrl(@Valid @RequestBody DocumentDownloadHttpRequest request,
                                                                       @RequestAttribute("attributes") Map<String, Object> attributes) {

        // Enforce guard for permission to user for downloading
        permissionGuard.canUserDownload(attributes, "/api/v1/documents/download");

        // If permission is cleared, then start download
        DocumentDownloadHttpResponse downloadUrl = documentMapper.toDocumentDownloadHttpResponse(
                documentService.generateDownloadUrl(request.getStorageKey())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(downloadUrl);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> downloadDocument(@RequestAttribute("attributes") Map<String, Object> attributes,
                                             @RequestParam("storageKey") String storageKey) {

        // Enforce guard for permission to user for deletion
        permissionGuard.canUserDelete(attributes, "/api/v1/documents/delete");

        // If permission is cleared, then delete document
        documentService.deleteDocument(storageKey);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}