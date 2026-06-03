package com.loan_org.document_service.document.repository;

import com.loan_org.document_service.document.model.DocumentMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentMetadata, String> {
    List<DocumentMetadata> findByApplicationId(String applicationId);
}