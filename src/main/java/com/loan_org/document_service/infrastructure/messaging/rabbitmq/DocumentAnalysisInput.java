package com.loan_org.document_service.infrastructure.messaging.rabbitmq;

public record DocumentAnalysisInput(
        String documentId,
        String storageKey,
        String documentType
) {}
