package com.loan_org.document_service.infrastructure.web.guard;

import com.loan_org.document_service.infrastructure.web.exception.classes.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PermissionGuardImpl implements PermissionGuard {

    @Value("${aws.webhook.secret-token}")
    private String expectedSecretToken;

    @Override
    public void canUserUpload(Map<String, Object> attributes, String endpoint) {
        if(attributes.get("document:upload") == null) {
            throw new PermissionDeniedException(endpoint,
                    "The user does not have the permission to upload document. Check your permissions or contact administrator");
        }
    }

    @Override
    public void canUserDownload(Map<String, Object> attributes, String endpoint) {
        if(attributes.get("document:download") == null) {
            throw new PermissionDeniedException(endpoint,
                    "The user does not have the permission to download document. Check your permission or contact administrator");
        }
    }

    @Override
    public void confirmMinIOEntry(String authToken, String endpoint) {
        if (authToken == null || !authToken.contains(expectedSecretToken)) {
            throw new PermissionDeniedException(endpoint,
                    "Unauthorized entry into the MinIO callback. Reporting to administrator.");
        }
    }


}
