package com.loan_org.document_service.infrastructure.messaging.rabbitmq;

import com.loan_org.document_service.document.model.DocumentMetadata;
import com.loan_org.document_service.document.port.DocumentScanner;
import com.loan_org.document_service.infrastructure.messaging.rabbitmq.ingestion.DocumentAnalysisInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentScannerRabbitMQImpl implements DocumentScanner {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void startScan(DocumentMetadata documentData) {
        log.info("[DOCUMENT_SCANNER] Queueing scan request for Document ID: {}", documentData.getId());

        DocumentAnalysisInput eventPayload = new DocumentAnalysisInput(
                documentData.getId(), documentData.getStorageKey(), documentData.getDocumentType().toString()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DOCS_EXCHANGE,
                RabbitMQConfig.ROUTING_DOC_UPLOADED,
                eventPayload
        );

        log.info("[DOCUMENT_SCANNER] Successfully dispatched document with id: {} to processing exchange.", documentData.getId());
    }
}