package com.bhnatiuk.uni.bookstore.backend.config.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtLeastOneFieldValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneField {
    String message() default "Request must contain at least one non-empty field";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
