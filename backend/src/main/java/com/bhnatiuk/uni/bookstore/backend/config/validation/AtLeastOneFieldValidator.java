package com.bhnatiuk.uni.bookstore.backend.config.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneFieldValidator implements ConstraintValidator<AtLeastOneField, PartialUpdate> {

    @Override
    public boolean isValid(PartialUpdate request, ConstraintValidatorContext context) {
        if (request == null) return false;
        return request.hasAtLeastOneNonBlankField();
    }
}
