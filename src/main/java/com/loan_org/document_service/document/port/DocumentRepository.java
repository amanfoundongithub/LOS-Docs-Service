package com.loan_org.document_service.document.port;

import com.loan_org.document_service.document.model.DocumentMetadata;

import java.util.List;
import java.util.Optional;

/**
 * Defines a port service saying that we can manipulate the document data in the desired
 * database, as defined in the function signatures.
 * <p>
 * This contract is used to connect the domain logic with the infrastructure implementation,
 * as per the hexagonal pattern.
 * </p>
 * @author amanfoundongithub
 * @version 2.0.0
 */
public interface DocumentRepository {

    /**
     * Defines a contract to save data into the database
     *
     * @param data The data to save in database
     * @return The data with updated id
     */
    DocumentMetadata           save(DocumentMetadata data);

    /**
     * Fetches the document bu the unique ID
     *
     * @param id The id of the database
     * @return The document with the id
     */
    Optional<DocumentMetadata> findById(String id);

    /**
     * Fetches all the documents for the given application ID
     *
     * @param applicationId The application ID to search for
     * @return The list of document data for the given application ID
     */
    List<DocumentMetadata>     findByApplicationId(String applicationId);

    /**
     * Fetches the document by the storage Key
     *
     * @param storageKey The storage Key for the application
     * @return The document corresponding to the storage key
     */
    Optional<DocumentMetadata> findByStorageKey(String storageKey);
}
