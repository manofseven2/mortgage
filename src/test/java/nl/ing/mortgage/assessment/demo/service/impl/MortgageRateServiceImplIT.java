package nl.ing.mortgage.assessment.demo.service.impl;

import jakarta.validation.constraints.NotNull;
import nl.ing.mortgage.assessment.demo.IntegrationTest;
import nl.ing.mortgage.assessment.demo.domain.MortgageRate;
import nl.ing.mortgage.assessment.demo.repository.MortgageRateRepository;
import nl.ing.mortgage.assessment.demo.service.MortgageRateService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@ActiveProfiles(value = {"local"})
class MortgageRateServiceImplIT {

    private static final @NotNull Double INTEREST_RATE = 2.0;
    private static final @NotNull Integer MATURITY_PERIOD = 144;
    private static final @NotNull Instant LAST_UPDATE = Instant.now();

    @Autowired
    private MortgageRateRepository mortgageRateRepository;

    @Autowired
    private MortgageRateService mortgageRateService;

    private MortgageRate mortgageRate;

    @BeforeEach
    public void setup() {
        mortgageRate = MortgageRate.builder().interestRate(INTEREST_RATE).lastUpdate(LAST_UPDATE).maturityPeriod(MATURITY_PERIOD).build();
    }

    @AfterEach
    public void cleanup() {
        mortgageRateRepository.deleteAll();
    }

    @Test
    @Transactional
    void save() {
        var saved = mortgageRateService.save(mortgageRate);
        var fetched = mortgageRateRepository.findById(saved.getId()).orElseThrow();
        assertThat(fetched.getInterestRate()).isEqualTo(INTEREST_RATE);
        assertThat(fetched.getMaturityPeriod()).isEqualTo(MATURITY_PERIOD);
        assertThat(fetched.getLastUpdate()).isEqualTo(LAST_UPDATE);
    }

    @Test
    void findOne() {
        mortgageRate = mortgageRateRepository.save(mortgageRate);

        var fetched = mortgageRateService.findOne(mortgageRate.getId()).orElseThrow();

        assertThat(fetched.getId()).isEqualTo(mortgageRate.getId());
        assertThat(fetched.getInterestRate()).isEqualTo(INTEREST_RATE);
        assertThat(fetched.getMaturityPeriod()).isEqualTo(MATURITY_PERIOD);
        assertThat(fetched.getLastUpdate()).isEqualTo(LAST_UPDATE);
    }

    @Test
    void findAll() {
        mortgageRate = mortgageRateRepository.save(mortgageRate);
        Pageable pageable = PageRequest.of(0, 20);
        var result = mortgageRateRepository.findAll(pageable);
        assertNotNull(result);
        assertTrue(result.hasContent());
        assertThat(result.getContent().stream().filter(a -> a.equals(mortgageRate)).toList()).isNotEmpty();
    }

    @Test
    void getMortgageRateByMaturityPeriod() {
        mortgageRate = mortgageRateRepository.save(mortgageRate);
        var list = mortgageRateRepository.findAll().stream().filter(a -> a.getMaturityPeriod() ==MATURITY_PERIOD).toList();
        assertThat(list.size()).isGreaterThan(1); // checks that there are more than one entity for this maturity period
        var result = mortgageRateService.getLatestMortgageRateByMaturityPeriod(MATURITY_PERIOD);
        assertFalse(result.isEmpty());
        var retrieved = result.orElseThrow();
        assertThat(retrieved.getId()).isEqualTo(mortgageRate.getId());
    }


    @Test
    void findAllCurrentMortgageRates() {
        //There are more than one entry for maturity period 144
        mortgageRate = mortgageRateRepository.save(mortgageRate);
        Pageable pageable = PageRequest.of(0, 20);
        var result = mortgageRateRepository.findAllCurrentMortgageRates(pageable);
        assertNotNull(result);
        assertTrue(result.hasContent());
        //Test will pass when service returns the latest one which is recently added in this test
        assertThat(result.getContent().stream()
                .filter(a -> a.getMaturityPeriod() == mortgageRate.getMaturityPeriod()).toList().size())
                .isEqualTo(1);
    }

    @Test
    public void testCacheableMethod() {
        // First call - should query the database
        var rate1 = mortgageRateService.getLatestMortgageRateByMaturityPeriod(MATURITY_PERIOD);

        // Second call - should hit the cache
        var rate2 = mortgageRateService.getLatestMortgageRateByMaturityPeriod(MATURITY_PERIOD);

        assertNotNull(rate1);
        assertNotNull(rate2);
        assertEquals(rate1, rate2);
    }
}