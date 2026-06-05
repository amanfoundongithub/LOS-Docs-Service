package com.loan_org.document_service.document.repository;

import com.loan_org.document_service.document.model.DocumentMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Defines a MongoDB interface to interact with the Document repository.
 *
 * @author amanfoundongithub
 * @version 1.0.0
 */
@Repository
public interface DocumentRepository extends MongoRepository<DocumentMetadata, String> {
    List<DocumentMetadata> findByApplicationId(String applicationId);
    DocumentMetadata       findByStorageKey(String storageKey);
}