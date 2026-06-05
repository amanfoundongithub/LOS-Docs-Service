package com.loan_org.document_service.infrastructure.persistence.mongo;

import com.loan_org.document_service.document.model.DocumentMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Mongo persistence repository defined for DocumentMetadata
 *
 * @author amanfoundongithub
 * @version 1.0.0
 */
public interface SpringDataDocumentMongoRepository extends MongoRepository<DocumentMetadata, String> {
    List<DocumentMetadata>     findByApplicationId(String applicationId);
    Optional<DocumentMetadata> findByStorageKey(String storageKey);
}
