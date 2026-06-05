package com.loan_org.document_service.document.port;

/**
 * Defines a port service saying that we have Document storage service with the given
 * functions defined as part of the signature.
 * <p>
 * This contract is used to connect the domain logic with the infrastructure implementation,
 * as per the hexagonal pattern.
 * </p>
 * @author amanfoundongithub
 * @version 1.0.0
 */
public interface DocumentStorageService {

    /**
     * Generates a upload URL for a defined storageKey to allow for the service
     * vendor to generate a space for uploading.
     *
     * @param storageKey The key to upload to
     * @return The link to upload the actual document
     */
    String generateUploadURL(String storageKey);

    /**
     * Generates a Download URL to download the document.
     *
     * @param storageKey The key to download from
     * @return The link to download the actual document
     */
    String generateDownloadURL(String storageKey);
}
