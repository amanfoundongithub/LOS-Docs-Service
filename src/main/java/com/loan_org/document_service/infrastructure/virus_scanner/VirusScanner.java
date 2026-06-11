package com.loan_org.document_service.infrastructure.virus_scanner;

import java.io.InputStream;

public interface VirusScanner {

    /**
     * Scans a file stream for malware, viruses, and executable vulnerabilities.
     *
     * @param fileStream The raw binary data stream fetched from storage.
     * @return true if the file is completely clean, false if infected.
     */
    boolean scan(InputStream fileStream);

}
