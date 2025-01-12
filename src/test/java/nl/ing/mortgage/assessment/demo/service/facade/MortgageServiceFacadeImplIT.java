package nl.ing.mortgage.assessment.demo.service.facade;

import jakarta.validation.constraints.NotNull;
import nl.ing.mortgage.assessment.demo.IntegrationTest;
import nl.ing.mortgage.assessment.demo.domain.MortgageRate;
import nl.ing.mortgage.assessment.demo.exception.BusinessException;
import nl.ing.mortgage.assessment.demo.repository.MortgageRateRepository;
import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@IntegrationTest
class MortgageServiceFacadeImplIT {

    @Autowired
    private MortgageServiceFacadeImpl facade;

    @Autowired
    private MortgageRateRepository mortgageRateRepository;

    private static final @NotNull Double INTEREST_RATE = 2.0;
    private static final @NotNull Integer MATURITY_PERIOD = 120;
    private static final @NotNull Instant LAST_UPDATE = Instant.now();

    private MortgageRate mortgageRate;

    @BeforeEach
    public void setup() {
        mortgageRate = MortgageRate.builder().interestRate(INTEREST_RATE).lastUpdate(LAST_UPDATE).maturityPeriod(MATURITY_PERIOD).build();
    }
    @Test
    void calculate_withoutError() {
        MortgageCheckDto dto = new MortgageCheckDto(20000.0, 120, 200000.0, 250000.0, new HashMap<>());
        mortgageRateRepository.save(mortgageRate);
        var result = facade.calculate(dto);
        assertEquals(1840.27, result.monthlyCost());
    }

    @Test
    void calculate_withError_noMortgageRateFound() {
        MortgageCheckDto dto = new MortgageCheckDto(20000.0, 123, 200000.0, 250000.0, new HashMap<>());
        assertThrows(BusinessException.class, () -> facade.calculate(dto));
    }
}