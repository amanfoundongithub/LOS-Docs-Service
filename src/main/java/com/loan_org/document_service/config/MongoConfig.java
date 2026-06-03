package com.loan_org.document_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Defines configuration enablers for MongoDB connection, and enabling mongo
 * auditing for record safety purposes.
 *
 * @author amanfoundongithub
 */
@EnableMongoAuditing
@Configuration
public class MongoConfig {
    // Define MongoDB configuration here
}
