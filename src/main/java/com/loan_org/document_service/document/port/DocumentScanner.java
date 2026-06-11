package com.loan_org.document_service.document.port;

import com.loan_org.document_service.document.model.DocumentMetadata;

/**
 * Defines a contract for document scan after it is uploaded
 *
 * @author amanfoundongithub
 * @version 2.0.0
 */
public interface DocumentScanner {

    /**
     * Starts the scan process
     *
     * @param documentData The data from MongoDB
     */
    void startScan(DocumentMetadata documentData);

}
