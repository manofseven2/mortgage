package nl.ing.mortgage.assessment.demo.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * it checks that the requester has enough income to get a mortgage
 */
@Documented
@Constraint(validatedBy = IncomeValidator.class)
@Target( { ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IncomeConstraint {
    String message() default "Mortgage should be less than four times of income.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
