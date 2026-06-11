package com.loan_org.document_service.infrastructure.web.guard;

import java.util.Map;

public interface PermissionGuard {

    void canUserUpload(Map<String, Object> attributes, String endpoint);

    void canUserDownload(Map<String, Object> attributes, String endpoint);

}
