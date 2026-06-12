package com.loan_org.document_service.infrastructure.web.dto.update;

import lombok.Builder;

@Builder
public record DocumentUpdateHttpResponse(
        String updateUrl
) {}
