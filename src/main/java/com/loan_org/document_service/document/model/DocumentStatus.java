package com.loan_org.document_service.document.model;

public enum DocumentStatus {
    /**
     * Document record created and presigned upload URL generated.
     * Waiting for the client to upload the file to cloud storage.
     */
    PENDING,

    /**
     * Client confirmed successful upload.
     * File is in storage and queued for processing (virus scanning/validation).
     */
    UPLOADED,

    /**
     * File has passed virus scanning and structure validation.
     */
    PROCESSING,

    /**
     * Document successfully processed, classified and virus
     * Ready for verification.
     */
    AVAILABLE,

    /**
     * Document failed a step (e.g., malware detected, corrupt file, or unreadable).
     */
    REJECTED,

    /**
     * Archived documents
     */
    ARCHIVED
}