package com.loan_org.document_service.document.dto;

/**
 * Defines a record class to store the configuration related to uploading
 * of document in our domain class
 *
 * @param applicationId The application ID of the loan
 * @param documentType The type of document we are uploading
 * @param fileName Name of the file we are uploading
 * @param fileSize Size of the file we are uploading
 *
 * @author amanfoundongithub
 * @version 1.0.0
 */
public record UploadDocumentCommand(
        String applicationId,
        String documentType,
        String fileName,
        long fileSize
) {}
