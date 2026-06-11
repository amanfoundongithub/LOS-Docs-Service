package com.loan_org.document_service.document.constraints.decorator;

import com.loan_org.document_service.infrastructure.decorator.ValidateContentTypeImpl;
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
@Constraint(validatedBy = ValidateContentTypeImpl.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateContentType {
    String                     message() default "Invalid document content type. Supported formats are: PDF, JPEG, PNG.";
    Class<?>[]                 groups() default {};
    Class<? extends Payload>[] payload() default {};
}
