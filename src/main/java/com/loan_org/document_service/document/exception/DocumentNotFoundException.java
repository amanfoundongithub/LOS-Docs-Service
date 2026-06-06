package com.loan_org.document_service.document.exception;

public class DocumentNotFoundException extends RuntimeException {

    public final String key;
    public final String value;

    private static final String DOCUMENT_NOT_FOUND_TEMPLATE =
            "No document found in record for %s with value %s";

    public DocumentNotFoundException(String key, String value) {
        super(String.format(DOCUMENT_NOT_FOUND_TEMPLATE, key, value));
        this.key = key;
        this.value = value;
    }

}
