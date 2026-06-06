package com.loan_org.document_service.infrastructure.web.controller;

import com.loan_org.document_service.infrastructure.web.dto.*;
import com.loan_org.document_service.document.service.DocumentService;
import com.loan_org.document_service.infrastructure.web.exception.classes.PermissionDeniedException;
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

    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadResponse> initializeUpload(@Valid @RequestBody UploadRequest request,
                                                                   @RequestAttribute("userRole") String userRole,
                                                                   @RequestAttribute("userId") String userId,
                                                                   @RequestAttribute("canUpload") boolean canUpload) {
        if (!canUpload) {
            throw new PermissionDeniedException("/api/v1/documents/upload", userId, "No permission found for `document:upload` for user:" + userId);
        }

        DocumentUploadResponse response = documentMapper.toControllerResponse(
                documentService.initializeUpload(
                        documentMapper.toCommand(request)
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByApplication(@PathVariable String applicationId) {
        List<DocumentResponse> responseList = documentMapper.toDocumentResponses(
                documentService.getDocumentsByApplication(applicationId)
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PostMapping("/download")
    public ResponseEntity<DocumentDownloadResponse> getDownloadUrl(@Valid @RequestBody DocumentDownloadRequest request) {
        DocumentDownloadResponse downloadUrl = documentMapper.toDocumentDownloadResponse(
                documentService.generateDownloadUrl(request.storageKey())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(downloadUrl);
    }
}