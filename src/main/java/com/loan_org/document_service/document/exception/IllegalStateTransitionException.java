package com.loan_org.document_service.document.exception;

public class IllegalStateTransitionException extends RuntimeException {

    public final String endpoint;

    public IllegalStateTransitionException(String message, String endpoint) {
        super(message);
        this.endpoint = endpoint;
    }
}
