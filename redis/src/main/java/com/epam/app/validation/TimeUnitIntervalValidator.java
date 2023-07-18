package com.epam.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TimeUnitIntervalValidator implements ConstraintValidator<TimeUnitInterval, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return Arrays.stream(TimeUnit.values())
                .map(Enum::name)
                .anyMatch(value::equals);
    }
}
