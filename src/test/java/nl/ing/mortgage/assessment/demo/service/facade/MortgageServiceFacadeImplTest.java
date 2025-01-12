package nl.ing.mortgage.assessment.demo.service.facade;

import nl.ing.mortgage.assessment.demo.domain.MortgageRate;
import nl.ing.mortgage.assessment.demo.exception.BusinessException;
import nl.ing.mortgage.assessment.demo.service.MortgageRateService;
import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class MortgageServiceFacadeImplTest {

    @Mock
    private MortgageRateService mortgageRateService;
    private MortgageServiceFacadeImpl facade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        facade = new MortgageServiceFacadeImpl(mortgageRateService);
    }

    @Test
    void calculate_withoutError() {
        MortgageCheckDto dto = new MortgageCheckDto(20000.0, 120, 200000.0, 250000.0, new HashMap<>());
        when(mortgageRateService.getLatestMortgageRateByMaturityPeriod(dto.maturityPeriod())).thenReturn(Optional.of(new MortgageRate(1L, 120, 2.0, Instant.now())));
        var result = facade.calculate(dto);
        assertEquals(1840.27, result.monthlyCost());
    }

    @Test
    void calculate_withError_noMortgageRateFound() {
        MortgageCheckDto dto = new MortgageCheckDto(20000.0, 120, 200000.0, 250000.0, new HashMap<>());
        when(mortgageRateService.getLatestMortgageRateByMaturityPeriod(dto.maturityPeriod())).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> facade.calculate(dto));
    }
}