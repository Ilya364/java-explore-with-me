package ru.practicum.ewm.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class AfterTwoHoursValidator implements ConstraintValidator<AfterTwoHours, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        try {
            return value.isAfter(LocalDateTime.now().plusHours(2));
        } catch (NullPointerException e) {
            return true;
        }
    }

    @Override
    public void initialize(AfterTwoHours constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
