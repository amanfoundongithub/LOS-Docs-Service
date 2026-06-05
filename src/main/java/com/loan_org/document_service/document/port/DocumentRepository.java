package com.loan_org.document_service.document.port;

import com.loan_org.document_service.document.model.DocumentMetadata;

/**
 * Defines a port service saying that we can manipulate the document data in the desired
 * database, as defined in the function signatures.
 * <p>
 * This contract is used to connect the domain logic with the infrastructure implementation,
 * as per the hexagonal pattern.
 * </p>
 * @author amanfoundongithub
 * @version 1.0.0
 */
public interface DocumentRepository {

    /**
     * Defines a contract to save data into the database
     *
     * @param data The data to save in database
     * @return The data with updated id
     */
    DocumentMetadata save(DocumentMetadata data);

}
