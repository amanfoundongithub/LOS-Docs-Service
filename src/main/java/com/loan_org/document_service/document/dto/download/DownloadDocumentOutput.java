package com.loan_org.document_service.document.dto.download;

import lombok.Builder;

import java.time.Instant;

/**
 * Defines the output for the document downloading
 *
 * @param downloadUrl
 * @param validForMinutes
 * @param createdTimestamp
 */
@Builder
public record DownloadDocumentOutput(
        String downloadUrl,
        int validForMinutes,
        Instant createdTimestamp
) {}
