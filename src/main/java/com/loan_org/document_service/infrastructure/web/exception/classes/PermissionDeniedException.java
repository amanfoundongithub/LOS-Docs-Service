package com.loan_org.document_service.infrastructure.web.exception.classes;

public class PermissionDeniedException extends RuntimeException {

    public final String endpoint;
    public final String userId;
    public final String reason;

    public PermissionDeniedException(String endpoint, String userId, String reason) {
        super(reason);
        this.endpoint = endpoint;
        this.userId = userId;
        this.reason = reason;
    }

    public PermissionDeniedException(String endpoint, String reason) {
        super(reason);
        this.endpoint = endpoint;
        this.userId = "";
        this.reason = reason;
    }

}
