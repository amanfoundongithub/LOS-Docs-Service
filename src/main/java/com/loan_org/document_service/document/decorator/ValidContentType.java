package com.loan_org.document_service.document.decorator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ContentTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidContentType {
    String                     message() default "Invalid document content type. Supported formats are: PDF, JPEG, PNG.";
    Class<?>[]                 groups() default {};
    Class<? extends Payload>[] payload() default {};
}
