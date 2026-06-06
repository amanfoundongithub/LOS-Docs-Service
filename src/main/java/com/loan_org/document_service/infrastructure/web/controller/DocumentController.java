package com.loan_org.document_service.infrastructure.web.controller;

import com.loan_org.document_service.document.dto.DocumentResponse;
import com.loan_org.document_service.infrastructure.web.dto.DocumentUploadResponse;
import com.loan_org.document_service.infrastructure.web.dto.UploadRequest;
import com.loan_org.document_service.document.service.DocumentService;
import com.loan_org.document_service.infrastructure.web.exception.classes.PermissionDeniedException;
import com.loan_org.document_service.infrastructure.web.mapper.DocumentObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadResponse> initializeUpload(@Valid @RequestBody UploadRequest request,
                                                                   @RequestAttribute("userRole") String userRole,
                                                                   @RequestAttribute("userId") String userId,
                                                                   @RequestAttribute("canUpload") boolean canUpload) {
        if (!canUpload) {
            throw new PermissionDeniedException("/upload", userId, "No permission found for `document:upload` for user:" + userId);
        }

        DocumentUploadResponse response = documentMapper.toControllerResponse(
                documentService.initializeUpload(
                        documentMapper.toCommand(request)
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByApplication(@PathVariable String applicationId) {
        log.info("Received request to fetch documents for loan application: {}", applicationId);
        List<DocumentResponse> responseList = documentService.getDocumentsByApplication(applicationId);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Map<String, String>> getDownloadUrl(@PathVariable String id) {
        log.info("Received request to fetch secure download link for document ID: {}", id);
        String downloadUrl = documentService.generateDownloadUrl(id);
        return ResponseEntity.ok(Map.of("downloadUrl", downloadUrl));
    }
}