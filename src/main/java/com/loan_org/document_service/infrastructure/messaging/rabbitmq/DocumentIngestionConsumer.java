package com.loan_org.document_service.infrastructure.messaging.rabbitmq;

import com.loan_org.document_service.document.model.DocumentStatus;
import com.loan_org.document_service.document.port.DocumentRepository;
import com.loan_org.document_service.document.port.DocumentStorageService;
import com.loan_org.document_service.infrastructure.messaging.PipelineFailureException;
import com.loan_org.document_service.infrastructure.virus_scanner.VirusScanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentIngestionConsumer {

    private final DocumentRepository     documentRepository;
    private final DocumentStorageService documentStorageService;
    private final VirusScanner           virusScanner;

    @RabbitListener(queues = RabbitMQConfig.INBOUND_PROCESSING_QUEUE)
    public void processUploadDocument(DocumentAnalysisInput input) {

        // Log the acknowledgment
        log.info("[DOCUMENT_INGESTION_CONSUMER] Starting scanning execution for Doc ID: {}", input.documentId());

        // Update to processing
        updateStatus(input.documentId(), DocumentStatus.PROCESSING);

        // Start scanning...
        try (InputStream fileStream = documentStorageService.downloadStream(input.storageKey())) {

            boolean isClean = virusScanner.scan(fileStream);
            if (isClean) {
                log.info("[DOCUMENT_INGESTION_CONSUMER] Document {} passed preliminary security checks. Marking as AVAILABLE.", input.documentId());
                updateStatus(input.documentId(), DocumentStatus.AVAILABLE);

                // TODO: Emit to Verification service to start their work. Next release...

            } else {
                log.error("[DOCUMENT_INGESTION_CONSUMER] Document {} failed preliminary security checks. Flagging as REJECTED.", input.documentId());
                updateStatus(input.documentId(), DocumentStatus.REJECTED);
            }

        } catch (Exception ex) {
            log.error("Pipeline failure while running ingestion filters on document: {}",
                    input.documentId(), ex);
            throw new PipelineFailureException("Routing execution run to DLQ due to runtime error", ex);
        }
    }

    private void updateStatus(String documentId, DocumentStatus targetStatus) {
        documentRepository.findById(documentId).ifPresent(meta -> {
            meta.setStatus(targetStatus);
            documentRepository.save(meta);
        });
    }
}
