package com.loan_org.document_service.document.dto.upload;

import com.loan_org.document_service.document.model.DocumentStatus;
import lombok.Builder;

/**
 * Defines a contract to mark the document upload as completed.
 *
 * @param documentId  The ID of database
 * @param fileName    The file name
 * @param fileType    The file type
 * @param status      The current status of upload
 * @param uploadUrl   The storage URL
 * @param instruction The additional instruction on how to use
 */
@Builder
public record UploadDocumentOutput(
        String         documentId,
        String         fileName,
        String         fileType,
        DocumentStatus status,
        String         uploadUrl,
        String         instruction
) {}
