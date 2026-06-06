package com.loan_org.document_service.infrastructure.web.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record DocumentDownloadResponse(
        String downloadUrl,
        int validForMinutes,
        Instant createdTimestamp
){}
