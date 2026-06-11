package com.loan_org.document_service.infrastructure.messaging.rabbitmq.verification;

public interface DocumentVerificationPublisher {
    void broadcastDocumentUploaded(DocumentVerificationInput input);
}
