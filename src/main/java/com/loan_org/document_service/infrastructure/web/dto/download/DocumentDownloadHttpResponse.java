package com.loan_org.document_service.infrastructure.web.dto.download;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class DocumentDownloadHttpResponse {
    private String  downloadUrl;
    private int     validForMinutes;
    private Instant createdTimestamp;
}
