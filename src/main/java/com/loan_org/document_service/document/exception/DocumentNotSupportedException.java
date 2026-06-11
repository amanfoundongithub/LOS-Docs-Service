package com.loan_org.document_service.document.exception;

public class DocumentNotSupportedException extends RuntimeException {

    private static final String MIME_TYPE_NOT_VALID_TEMPLATE =
            "The MIME type: %s is not valid. The supported ones are PDF, JPG or JPEG.";

    public DocumentNotSupportedException(String type) {
        super(String.format(MIME_TYPE_NOT_VALID_TEMPLATE, type));
    }

}
