package com.loan_org.document_service.infrastructure.decorator;

import com.loan_org.document_service.document.constraints.decorator.ValidateContentType;
import com.loan_org.document_service.document.model.AllowedContentType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidateContentTypeImpl implements ConstraintValidator<ValidateContentType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return false;
        }

        try {
            AllowedContentType.fromMimeType(value.toLowerCase().trim());
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
