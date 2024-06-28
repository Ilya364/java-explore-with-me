package ru.practicum.ewm.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AfterTwoHoursValidator.class)
@Documented
@ReportAsSingleViolation
public @interface AfterTwoHours {
    String message() default "Can't be blank.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
