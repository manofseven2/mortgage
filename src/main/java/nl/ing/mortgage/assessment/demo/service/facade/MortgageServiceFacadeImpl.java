package nl.ing.mortgage.assessment.demo.service.facade;

import lombok.AllArgsConstructor;
import nl.ing.mortgage.assessment.demo.exception.BusinessException;
import nl.ing.mortgage.assessment.demo.service.MortgageRateService;
import nl.ing.mortgage.assessment.demo.service.MortgageServiceFacade;
import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckDto;
import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckResponseDto;
import org.springframework.stereotype.Service;

import static nl.ing.mortgage.assessment.demo.exception.BusinessExceptionMessages.MORTGAGE_DATA_COULD_NOT_BE_FOUND;
import static nl.ing.mortgage.assessment.demo.util.Utility.calculateMonthlyCost;

@Service
@AllArgsConstructor
public class MortgageServiceFacadeImpl implements MortgageServiceFacade {
    private final MortgageRateService mortgageRateService;

    @Override
    public MortgageCheckResponseDto calculate(MortgageCheckDto dto) {
        var mortgageRateOptional = mortgageRateService.getLatestMortgageRateByMaturityPeriod(dto.maturityPeriod());

        var mortgage = mortgageRateOptional.orElseThrow(() -> new BusinessException(MORTGAGE_DATA_COULD_NOT_BE_FOUND));

        var monthlyCost = calculateMonthlyCost(dto.loanValue(), mortgage.getInterestRate(), mortgage.getMaturityPeriod());
        return new MortgageCheckResponseDto(true, monthlyCost);
    }
}
