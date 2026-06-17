package com.loan_org.document_service.infrastructure.messaging.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ─── STANDARD EXCHANGES & QUEUES ───
    public static final String DOCS_EXCHANGE = "docs.exchange";
    public static final String VERIFICATION_EXCHANGE = "verification.exchange";
    public static final String INBOUND_PROCESSING_QUEUE = "docs.uploaded.processing.queue";

    // ─── DEAD LETTER INFRASTRUCTURE ───
    public static final String DOCS_DLX = "docs.dlx";
    public static final String INBOUND_PROCESSING_DLQ = "docs.uploaded.processing.dlq";

    // ─── ROUTING KEYS ───
    public static final String ROUTING_DOC_UPLOADED = "document.status.uploaded";
    public static final String ROUTING_DEAD_LETTER = "document.deadletter";

    // ─── INFRASTRUCTURE CONVERTERS ───
    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // ─── CORE EXCHANGE BEANS ───
    @Bean
    public TopicExchange docsExchange() {
        return new TopicExchange(DOCS_EXCHANGE);
    }

    @Bean
    public TopicExchange verificationExchange() {
        return new TopicExchange(VERIFICATION_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange docsDeadLetterExchange() {
        return new TopicExchange(DOCS_DLX);
    }

    // ─── DEAD LETTER QUEUE & BINDING ───
    @Bean
    public Queue inboundProcessingDlq() {
        return QueueBuilder.durable(INBOUND_PROCESSING_DLQ).build();
    }

    @Bean
    public Binding bindDeadLetterQueue(Queue inboundProcessingDlq, TopicExchange docsDeadLetterExchange) {
        return BindingBuilder.bind(inboundProcessingDlq)
                .to(docsDeadLetterExchange)
                .with(ROUTING_DEAD_LETTER);
    }

    // ─── INBOUND PROCESSING QUEUE WITH DLX BINDING ───
    @Bean
    public Queue inboundProcessingQueue() {
        return QueueBuilder.durable(INBOUND_PROCESSING_QUEUE)
                .withArgument("x-dead-letter-exchange", DOCS_DLX)
                .withArgument("x-dead-letter-routing-key", ROUTING_DEAD_LETTER)
                .build();
    }

    @Bean
    public Binding bindInboundProcessingQueue(Queue inboundProcessingQueue, TopicExchange docsExchange) {
        return BindingBuilder.bind(inboundProcessingQueue)
                .to(docsExchange)
                .with(ROUTING_DOC_UPLOADED);
    }
}