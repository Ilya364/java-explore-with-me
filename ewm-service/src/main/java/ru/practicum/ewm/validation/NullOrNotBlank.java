package ru.practicum.ewm.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrNotBlankValidator.class)
@Documented
@ReportAsSingleViolation
public @interface NullOrNotBlank {
    String message() default "Can't be blank.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
