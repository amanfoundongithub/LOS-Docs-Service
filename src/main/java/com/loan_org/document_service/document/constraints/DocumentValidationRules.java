package com.loan_org.document_service.document.constraints;

/**
 * Centralized compilation-safe constants holding validation boundaries and user-facing
 * validation failure messages for document tracking constraints.
 *
 * @author amanfoundongithub
 * @version 2.0.0
 */
public final class DocumentValidationRules {

    public static final String APPLICATION_ID_NOT_BLANK_MESSAGE =
          "Application ID is required to link your document with the loan application you " +
                  "are currently processing.";


    public static final String CUSTOMER_ID_NOT_BLANK_MESSAGE =
            "Customer ID is required to ensure that we can trace the customer who took the " +
                    "loan and whose credentials are getting uploaded.";

    public static final String DOCUMENT_TYPE_NOT_BLANK_MESSAGE =
            "Please pass the document type so that we can ascertain and confirm the authenticity " +
                    "of the requested document.";

    public static final String CONTENT_TYPE_NOT_BLANK_MESSAGE =
            "Please pass the content type of the document (in the correct MIME format) so that we can " +
                    "process the document accordingly.";

    public static final String CONTENT_TYPE_NOT_VALID_MESSAGE =
            "The content type passed is not correct. Please ensure it is a correct MIME type. Currently accepted MIME types are: " +
                    "application/pdf";

    public static final String FILE_NAME_NOT_BLANK_MESSAGE =
            "Please pass a valid file name to ensure that the document is correctly named, and kept for " +
                    "record";

    public static final long MIN_FILE_SIZE_IN_BYTES = 1024L;

    public static final String FILE_MINIMUM_SIZE_MESSAGE =
            "The minimum size of the file uploaded must be at least 1 KB.";

    private DocumentValidationRules() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}
