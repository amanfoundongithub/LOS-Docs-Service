package com.loan_org.document_service.document.dto.upload;

import com.loan_org.document_service.document.model.DocumentStatus;
import com.loan_org.document_service.infrastructure.web.dto.upload.DocumentUploadHttpResponse;
import lombok.Builder;

/**
 * This is a record class to give in response to user's request to create an instance metadata
 * of the {@link UploadDocumentCommand UploadDocumentCommand}, as returned by the {@link com.loan_org.document_service.document.service.DocumentService Document Service}
 * after the success.
 *
 * <p>
 *     <strong>(For the HTTP model, refer to {@link DocumentUploadHttpResponse DocumentUploadResponse} object.)</strong>
 * </p>
 *
 * @author amanfoundongithub
 * @version 2.0.0
 *
 * @param documentId  The ID of database
 * @param fileName    The file name
 * @param fileType    The file type
 * @param contentType The content type of upload
 * @param status      The current status of upload
 * @param uploadUrl   The storage URL
 * @param instruction The additional instruction on how to use
 */
@Builder
public record UploadDocumentOutput(
        String         documentId,
        String         fileName,
        String         fileType,
        String         contentType,
        DocumentStatus status,
        String         uploadUrl,
        String         instruction
) {}
