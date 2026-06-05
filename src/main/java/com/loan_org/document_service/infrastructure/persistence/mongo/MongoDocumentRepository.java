package com.loan_org.document_service.infrastructure.persistence.mongo;

import com.loan_org.document_service.document.model.DocumentMetadata;
import com.loan_org.document_service.document.port.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MongoDocumentRepository implements DocumentRepository {

    private final SpringDataDocumentMongoRepository mongoRepository;

    @Override
    public DocumentMetadata save(DocumentMetadata data) {
        return mongoRepository.save(data);
    }

    @Override
    public Optional<DocumentMetadata> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public List<DocumentMetadata> findByApplicationId(String applicationId) {
        return mongoRepository.findByApplicationId(applicationId);
    }

    @Override
    public Optional<DocumentMetadata> findByStorageKey(String storageKey) {
        return mongoRepository.findByStorageKey(storageKey);
    }

}
