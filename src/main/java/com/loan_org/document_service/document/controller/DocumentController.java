package com.loan_org.document_service.document.controller;

import com.loan_org.document_service.document.dto.DocumentResponse;
import com.loan_org.document_service.document.dto.DocumentUploadResponse;
import com.loan_org.document_service.document.dto.UploadRequest;
import com.loan_org.document_service.document.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    // Inject the service
    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadResponse> initializeUpload(@Valid @RequestBody UploadRequest request,
                                                                   @RequestAttribute("userRole") String userRole,
                                                                   @RequestAttribute("userId") String userId,
                                                                   @RequestAttribute("canUpload") boolean canUpload) {
        if (!canUpload) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized action");
        }

        if ("APPLICANT".equals(userRole) && !userId.equals(request.getApplicationId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }
        return ResponseEntity.ok(documentService.initializeUpload(request));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<DocumentResponse> confirmUpload(@PathVariable String id) {
        log.info("Received request to confirm upload for document ID: {}", id);
        DocumentResponse response = documentService.confirmUpload(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByApplication(@PathVariable String applicationId) {
        log.info("Received request to fetch documents for loan application: {}", applicationId);
        List<DocumentResponse> responseList = documentService.getDocumentsByApplication(applicationId);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}/download-url")
    public ResponseEntity<Map<String, String>> getDownloadUrl(@PathVariable String id) {
        log.info("Received request to fetch secure download link for document ID: {}", id);
        String downloadUrl = documentService.generateDownloadUrl(id);
        return ResponseEntity.ok(Map.of("downloadUrl", downloadUrl));
    }
}