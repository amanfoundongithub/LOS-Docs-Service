package com.loan_org.document_service.infrastructure.messaging;

public class PipelineFailureException extends RuntimeException {
    public PipelineFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
