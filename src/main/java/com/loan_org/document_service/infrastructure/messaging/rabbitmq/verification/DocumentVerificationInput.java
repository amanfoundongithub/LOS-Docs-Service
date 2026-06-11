package com.loan_org.document_service.infrastructure.messaging.rabbitmq.verification;

import jakarta.validation.constraints.NotBlank;

public record DocumentVerificationInput(

        @NotBlank(message = "documentId associated with Loan is required.")
        String documentId,

        @NotBlank(message = "storageKey associated with the document is required to be linked to this verification.")
        String storageKey,

        @NotBlank(message = "Document download URL is required to download the document.")
        String downloadUrl,

        @NotBlank(message = "Please provide the document type (e.g. AADHAAR, PAN) so that we can do analysis accordingly.")
        String documentType
) {}
