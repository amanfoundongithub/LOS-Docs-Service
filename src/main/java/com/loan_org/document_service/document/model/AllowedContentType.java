package com.loan_org.document_service.document.model;

import com.loan_org.document_service.document.exception.DocumentNotSupportedException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AllowedContentType {
    PDF("application/pdf"),
    JPG("image/jpeg"),
    PNG("image/png");

    private final String mimeType;

    public static AllowedContentType fromMimeType(String mimeType) {
        for (AllowedContentType type : values()) {
            if (type.getMimeType().equalsIgnoreCase(mimeType)) {
                return type;
            }
        }
        throw new DocumentNotSupportedException(mimeType);
    }

}
