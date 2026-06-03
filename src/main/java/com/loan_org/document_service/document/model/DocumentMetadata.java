package com.loan_org.document_service.document.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collection = "documents")
public class DocumentMetadata {

    @Id
    private String id;

    @Indexed
    private String applicationId;
    private String documentType;
    private String fileName;
    private long fileSize;
    private String storageKey;

    @Indexed
    private DocumentStatus status;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Version
    private Long version;
}
