package nl.ing.mortgage.assessment.demo.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * It controls that the requested mortgage does not exceed the home value
 */
@Documented
@Constraint(validatedBy = MortgageAmountValidator.class)
@Target( { ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MortgageAmountConstraint {
    String message() default "Mortgage amount should be more than home value.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
