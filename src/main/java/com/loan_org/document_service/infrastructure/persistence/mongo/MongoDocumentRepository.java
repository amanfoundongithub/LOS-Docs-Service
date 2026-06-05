package com.loan_org.document_service.infrastructure.persistence.mongo;

import com.loan_org.document_service.document.model.DocumentMetadata;
import com.loan_org.document_service.document.port.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoDocumentRepository implements DocumentRepository {

    private final SpringDataDocumentMongoRepository mongoRepository;

    @Override
    public DocumentMetadata save(DocumentMetadata data) {
        return mongoRepository.save(data);
    }

}
