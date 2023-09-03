package com.company.web.wallet.validators;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureDate.FutureDateValidator.class)
public @interface FutureDate {
    String message() default "Expiration date must be in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class FutureDateValidator implements ConstraintValidator<FutureDate, LocalDate> {
        @Override
        public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
            if (value == null) {
                return true; // Let other validators handle null values
            }

            LocalDate now = LocalDate.now();
            return value.isAfter(now);
        }
    }
}
