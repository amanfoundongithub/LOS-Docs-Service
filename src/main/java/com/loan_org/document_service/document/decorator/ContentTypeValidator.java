package com.loan_org.document_service.document.decorator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class ContentTypeValidator implements ConstraintValidator<ValidContentType, String> {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }
        return ALLOWED_TYPES.contains(value.toLowerCase().trim());
    }

}
