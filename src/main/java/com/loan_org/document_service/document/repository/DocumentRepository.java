package com.loan_org.document_service.document.repository;

import com.loan_org.document_service.document.model.DocumentMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentMetadata, String> {

    /**
     * Finds all documents associated with a specific loan application.
     * Leverages the database index we designed earlier.
     */
    List<DocumentMetadata> findByApplicationId(String applicationId);
}