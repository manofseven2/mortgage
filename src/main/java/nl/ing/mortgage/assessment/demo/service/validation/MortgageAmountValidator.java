package nl.ing.mortgage.assessment.demo.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckDto;

@AllArgsConstructor
public class MortgageAmountValidator implements
        ConstraintValidator<MortgageAmountConstraint, MortgageCheckDto> {

    @Override
    public boolean isValid(MortgageCheckDto dto,
                           ConstraintValidatorContext cxt) {
        return dto.homeValue() >= dto.loanValue();
    }
}
