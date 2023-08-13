package com.company.web.wallet.validators;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureDate.FutureDateValidator.class)
public @interface FutureDate {
    String message() default "Expiration date must be in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class FutureDateValidator implements ConstraintValidator<FutureDate, LocalDateTime> {
        @Override
        public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
            if (value == null) {
                return true; // Let other validators handle null values
            }

            LocalDateTime now = LocalDateTime.now();
            return value.isAfter(now);
        }
    }
}
