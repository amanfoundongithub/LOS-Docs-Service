package com.loan_org.document_service.document.decorator;

import com.loan_org.document_service.infrastructure.decorator.ContentTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Defines the interface for the validation of contentType parameter in the request. Use this
 * annotator to decide on the valid content type that should be present in the request. All the
 * DTOs that require the "contentType" will pass the MIME and the decorator will match it against the
 * available ones.
 * <p>
 *     Decorator implementation is available in the `infrastructure/decorator` folder for reference.
 * </p>
 *
 *
 * @author amanfoundongithub
 * @version 2.0.0
 */
@Documented
@Constraint(validatedBy = ContentTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidContentType {
    String                     message() default "Invalid document content type. Supported formats are: PDF, JPEG, PNG.";
    Class<?>[]                 groups() default {};
    Class<? extends Payload>[] payload() default {};
}
