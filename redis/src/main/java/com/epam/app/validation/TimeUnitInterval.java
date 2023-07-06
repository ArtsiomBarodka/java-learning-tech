package com.epam.app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = TimeUnitIntervalValidator.class)
public @interface TimeUnitInterval {
    String message() default "The value doesn't match any of existing TimeUnits";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};
}
