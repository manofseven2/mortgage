package nl.ing.mortgage.assessment.demo.service.impl;

import jakarta.validation.constraints.NotNull;
import nl.ing.mortgage.assessment.demo.domain.MortgageRate;
import nl.ing.mortgage.assessment.demo.repository.MortgageRateRepository;
import nl.ing.mortgage.assessment.demo.service.MortgageRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class MortgageRateServiceImplTest {

    private final Long ID = 1L;
    private static final @NotNull Double INTEREST_RATE = 2.0;
    private static final @NotNull Integer MATURITY_PERIOD = 120;
    private static final @NotNull Instant LAST_UPDATE = Instant.now();

    @Mock
    private MortgageRateRepository mortgageRateRepository;

    private MortgageRateService mortgageRateService;

    private MortgageRate mortgageRate;
    @Mock
    private Pageable pageable;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mortgageRate = MortgageRate.builder().id(ID).interestRate(INTEREST_RATE)
                .lastUpdate(LAST_UPDATE).maturityPeriod(MATURITY_PERIOD).build();
        mortgageRateService = new MortgageRateServiceImpl(mortgageRateRepository);
    }

    @Test
    void save() {
        when(mortgageRateRepository.save(mortgageRate)).thenReturn(mortgageRate);
        var saved = mortgageRateService.save(mortgageRate);
        assertThat(saved.getId()).isEqualTo(ID);
        assertThat(saved.getInterestRate()).isEqualTo(INTEREST_RATE);
        assertThat(saved.getMaturityPeriod()).isEqualTo(MATURITY_PERIOD);
        assertThat(saved.getLastUpdate()).isEqualTo(LAST_UPDATE);
        verify(mortgageRateRepository, times(1)).save(mortgageRate);
    }

    @Test
    void findOne() {
        when(mortgageRateRepository.findById(any())).thenReturn(Optional.of(mortgageRate));
        var fetchedOptional = mortgageRateService.findOne(1L);
        var fetched = fetchedOptional.orElseThrow();
        assertThat(fetched.getId()).isEqualTo(ID);
        assertThat(fetched.getInterestRate()).isEqualTo(INTEREST_RATE);
        assertThat(fetched.getMaturityPeriod()).isEqualTo(MATURITY_PERIOD);
        assertThat(fetched.getLastUpdate()).isEqualTo(LAST_UPDATE);
        verify(mortgageRateRepository, times(1)).findById(ID);
    }

    @Test
    void findAll() {
        var list = new ArrayList<MortgageRate>();
        list.add(mortgageRate);
        Page<MortgageRate> page = new PageImpl<>(list);
        when(mortgageRateRepository.findAll(pageable)).thenReturn(page);
        var result = mortgageRateService.findAll(pageable);
        assertNotNull(result);
        assertTrue(result.hasContent());
        verify(mortgageRateRepository, times(1)).findAll(pageable);

    }

    @Test
    void findAllCurrentMortgageRates() {
        var list = new ArrayList<MortgageRate>();
        list.add(mortgageRate);
        Page<MortgageRate> page = new PageImpl<>(list);
        when(mortgageRateRepository.findAllCurrentMortgageRates(pageable)).thenReturn(page);
        var result = mortgageRateService.findAllCurrentMortgageRates(pageable);
        assertNotNull(result);
        assertTrue(result.hasContent());
        verify(mortgageRateRepository, times(1)).findAllCurrentMortgageRates(pageable);
    }

    @Test
    void getLatestMortgageRateByMaturityPeriod() {
        when(mortgageRateRepository.findTopByMaturityPeriodOrderByLastUpdateDesc(any()))
                .thenReturn(Optional.of(mortgageRate));
        var result = mortgageRateService.getLatestMortgageRateByMaturityPeriod(MATURITY_PERIOD);
        assertNotNull(result);
        assertThat(result.orElseThrow()).isEqualTo(mortgageRate);
        verify(mortgageRateRepository, times(1))
                .findTopByMaturityPeriodOrderByLastUpdateDesc(MATURITY_PERIOD);
    }
}