package gov.virginia.dmas.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = {StatusValidator.class})
public @interface ValidateStatus {

	String message() default "Invalid request: ReasonID is required from Closed to Open";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List {
        ValidateStatus[] value();
    }
}
