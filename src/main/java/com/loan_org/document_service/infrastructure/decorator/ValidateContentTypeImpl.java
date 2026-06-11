package com.loan_org.document_service.infrastructure.decorator;

import com.loan_org.document_service.document.constraints.decorator.ValidateContentType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class ValidateContentTypeImpl implements ConstraintValidator<ValidateContentType, String> {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }
        return ALLOWED_TYPES.contains(value.toLowerCase().trim());
    }

}
