package com.loan_org.document_service.document.port;

import com.loan_org.document_service.document.dto.UploadDocumentCommand;

/**
 * Defines a port service saying that we have a method to determine the AWS key
 * for uploading documents to storage service.
 * <p>
 * This contract is used to connect the domain logic with the infrastructure implementation,
 * as per the hexagonal pattern.
 * </p>
 * @author amanfoundongithub
 * @version 1.0.0
 */
public interface StorageKeyResolver {

    /**
     * Creates and returns the storage key for the storage service
     *
     * @param uploadRequest The request for upload
     * @return The string containing the string
     */
    String createStorageKey(UploadDocumentCommand uploadRequest);

}