package com.loan_org.document_service.document.port;

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
     * @param storageKey The storage key for the document to be scanned
     */
    void startScan(String storageKey);

}
