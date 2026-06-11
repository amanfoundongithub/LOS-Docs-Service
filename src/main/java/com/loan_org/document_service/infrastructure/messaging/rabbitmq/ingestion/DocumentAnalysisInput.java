package com.loan_org.document_service.infrastructure.messaging.rabbitmq.ingestion;

public record DocumentAnalysisInput(
        String documentId,
        String storageKey,
        String documentType
) {}
