package com.loan_org.document_service.document.port;

import java.io.InputStream;

/**
 * Defines a port service saying that we have Document storage service with the given
 * functions defined as part of the signature.
 * <p>
 * This contract is used to connect the domain logic with the infrastructure implementation,
 * as per the hexagonal pattern.
 * </p>
 * @author amanfoundongithub
 * @version 2.0.0
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
     * Gets the download validity in minutes
     *
     * @return The number of minutes the link is valid
     */
    int    getUploadDocumentValidityInMinutes();

    /**
     * Generates a Download URL to download the document.
     *
     * @param storageKey The key to download from
     * @return The link to download the actual document
     */
    String generateDownloadURL(String storageKey);

    /**
     * Generates an update URL to update the document.
     *
     * @param storageKey The key to update from
     * @return The link to update
     */
    String generateUpdateURL(String storageKey);

    /**
     * Gets the download validity in minutes.
     *
     * @return The number of minutes the link is valid
     */
    int    getDownloadDocumentValidityInMinutes();
    /**
     * Retrieves the raw binary byte stream of an object straight from cloud storage.
     * Crucial for asynchronous background processing pipelines (Virus scanning).
     *
     * @param storageKey The path key of the file inside the bucket.
     * @return InputStream containing the target file bytes.
     */
    InputStream downloadStream(String storageKey);
}
