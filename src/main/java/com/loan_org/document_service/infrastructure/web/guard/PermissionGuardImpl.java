package com.loan_org.document_service.infrastructure.web.guard;

import com.loan_org.document_service.infrastructure.web.exception.classes.PermissionDeniedException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PermissionGuardImpl implements PermissionGuard {

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


}
