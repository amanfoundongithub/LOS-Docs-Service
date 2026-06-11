package com.loan_org.document_service.infrastructure.messaging.rabbitmq.verification;

import com.loan_org.document_service.infrastructure.messaging.rabbitmq.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentVerificationPublisherImpl implements DocumentVerificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void broadcastDocumentUploaded(DocumentVerificationInput input) {
        log.info("Broadcasting document uploaded event for file key: {}", input.storageKey());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.VERIFICATION_EXCHANGE,
                RabbitMQConfig.ROUTING_DOC_UPLOADED,
                input
        );
    }

}
