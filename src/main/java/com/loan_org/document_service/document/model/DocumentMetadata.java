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

/**
 * Defines a MongoDB document pertaining to the metadata of an uploaded document. Useful
 * for keeping track of the document's life as well as the metadata associated with the
 * document.
 *
 * @author amanfoundongithub
 * @version 1.0.0
 */
@Data
@Builder
@Document(collection = "documents")
public class DocumentMetadata {

    @Id
    private String             id;             // ID associated with MongoDB

    @Indexed
    private String             applicationId;  // applicationId is required to tie the document with a specific application

    private DocumentType       documentType;   // Type of the document
    private String             fileName;       // Name of the document
    private AllowedContentType contentType;    // Content type of the document (currently only application/pdf)
    private long               fileSize;       // File size, to be determined for the document
    private String             storageKey;     // AWS storage key

    @Indexed
    private DocumentStatus     status;         // Status of the document in the application

    @CreatedDate
    private Instant            createdAt;      // Audit purpose, stores created timestamp

    @LastModifiedDate
    private Instant            updatedAt;     // Store last updated timestamp

    @Version
    private Long               version;       // Concurrency protection for database write
}
