package com.loan_org.document_service.infrastructure.web.dto;

import com.loan_org.document_service.document.model.DocumentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentUploadResponse {
    private String id;
    private String fileName;
    private String fileType;
    private String status;
    private String uploadUrl;

    @Builder.Default
    private String instruction = "Please use the `uploadUrl` field to upload the document as part of a " +
            "PUT request. Remember to attach the file as a binary, if using postman.";
}
