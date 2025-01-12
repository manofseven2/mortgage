package nl.ing.mortgage.assessment.demo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ing.mortgage.assessment.demo.IntegrationTest;
import nl.ing.mortgage.assessment.demo.domain.MortgageRate;
import nl.ing.mortgage.assessment.demo.repository.MortgageRateRepository;
import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckDto;
import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckResponseDto;
import nl.ing.mortgage.assessment.demo.web.error.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
class MortgageControllerIT {
    private static final String INTEREST_CHECK_API_URL = "/api/v1/mortgage-check";
    private static final String CURRENT_RATES_API_URL = "/api/v1/interest-rates";
    private static final Integer DEFAULT_MATURITY_PERIOD = 144;
    private static final Double DEFAULT_INTEREST_RATE = 1.0;
    private static final Instant DEFAULT_LAST_UPDATE = Instant.now();
    private static final String HEADER_X_TOTAL_COUNT = "X-Total-Count";

    private MortgageRate mortgageRate;

    @Autowired
    private MockMvc restMortgageRateMockMvc;

    @Autowired
    private MortgageRateRepository mortgageRateRepository;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    public void setup() {
        mortgageRate = MortgageRate.builder()
                .maturityPeriod(DEFAULT_MATURITY_PERIOD)
                .interestRate(DEFAULT_INTEREST_RATE)
                .lastUpdate(DEFAULT_LAST_UPDATE).build();
    }

    @Test
    @Transactional
    void getAllCurrentMortgageRates() throws Exception {
        mortgageRate = mortgageRateRepository.save(mortgageRate);
        // Get all the mortgageRateList
        restMortgageRateMockMvc
                .perform(get(CURRENT_RATES_API_URL + "?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem((int) mortgageRate.getId())))
                .andExpect(jsonPath("$.[*].maturityPeriod").value(hasItem(DEFAULT_MATURITY_PERIOD)))
                .andExpect(jsonPath("$.[*].interestRate").value(hasItem(DEFAULT_INTEREST_RATE.doubleValue())))
                .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
                .andExpect(header().exists(HEADER_X_TOTAL_COUNT));
    }

    @Test
    @Transactional
    void mortgageCheck() throws Exception {
        mortgageRateRepository.save(mortgageRate);
        MortgageCheckDto dto = new MortgageCheckDto(80000.0, DEFAULT_MATURITY_PERIOD, 150000.0, 200000.0);
        var response = om.readValue(
                restMortgageRateMockMvc
                        .perform(post(INTEREST_CHECK_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dto)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                MortgageCheckResponseDto.class
        );
        assertTrue(response.feasible());
        assertTrue(response.monthlyCost() > 0);
    }

    @Test
    @Transactional
    void mortgageCheck_negativeIncome() throws Exception {

        MortgageCheckDto dto = new MortgageCheckDto(-80000.0, DEFAULT_MATURITY_PERIOD, 150000.0, 200000.0);
        var response = om.readValue(
                restMortgageRateMockMvc
                        .perform(post(INTEREST_CHECK_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dto)))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ErrorResponse.class);
        assertEquals("Bad Request", response.error());
        assertThat(response.errors().stream().filter(e -> e.message().equals("must be greater than or equal to 0") && e.field().equals("income"))).isNotEmpty();
        assertThat(response.errors().stream().filter(e -> e.message().equals("Mortgage should be less than four times of income."))).isNotEmpty();
        assertThat(response.errors().size()).isEqualTo(2);
    }


    @Test
    @Transactional
    void mortgageCheck_negativeMaturityPeriod() throws Exception {

        MortgageCheckDto dto = new MortgageCheckDto(80000.0, -1, 150000.0, 200000.0);
        var response = om.readValue(
                restMortgageRateMockMvc
                        .perform(post(INTEREST_CHECK_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dto)))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ErrorResponse.class);
        assertEquals("Bad Request", response.error());
        assertThat(response.errors().stream().filter(e -> e.message().equals("must be greater than or equal to 1") && e.field().equals("maturityPeriod"))).isNotEmpty();
    }


    @Test
    @Transactional
    void mortgageCheck_negativeIncomeAndNegativeMaturityPeriod() throws Exception {

        MortgageCheckDto dto = new MortgageCheckDto(-80000.0, -144, 150000.0, 200000.0);
        var response = om.readValue(
                restMortgageRateMockMvc
                        .perform(post(INTEREST_CHECK_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dto)))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ErrorResponse.class);
        assertEquals("Bad Request", response.error());
        assertThat(response.errors().stream().filter(e -> e.message().equals("must be greater than or equal to 0") && e.field().equals("income"))).isNotEmpty();
        assertThat(response.errors().stream().filter(e -> e.message().equals("must be greater than or equal to 1") && e.field().equals("maturityPeriod"))).isNotEmpty();
        assertThat(response.errors().stream().filter(e -> e.message().equals("Mortgage should be less than four times of income."))).isNotEmpty();
        assertThat(response.errors().size()).isEqualTo(3);
    }


    @Test
    @Transactional
    void mortgageCheck_mortgageIsMoreThan4TimesOfIncome() throws Exception {

        MortgageCheckDto dto = new MortgageCheckDto(80000.0, DEFAULT_MATURITY_PERIOD, 400000.0, 500000.0);
        var response = om.readValue(
                restMortgageRateMockMvc
                        .perform(post(INTEREST_CHECK_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dto)))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ErrorResponse.class);
        assertEquals("Bad Request", response.error());
        assertThat(response.errors().stream().filter(e -> e.message().equals("Mortgage should be less than four times of income."))).isNotEmpty();
    }

    @Test
    @Transactional
    void mortgageCheck_mortgageExceedsHomeValue() throws Exception {

        MortgageCheckDto dto = new MortgageCheckDto(80000.0, DEFAULT_MATURITY_PERIOD, 300000.0, 200000.0);
        var response = om.readValue(
                restMortgageRateMockMvc
                        .perform(post(INTEREST_CHECK_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dto)))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ErrorResponse.class);
        assertEquals("Bad Request", response.error());
        assertThat(response.errors().stream().filter(e -> e.message().equals("Mortgage amount should be more than home value."))).isNotEmpty();
    }

    @Test
    @Transactional
    void mortgageCheck_notExistMaturityPeriod() throws Exception {
        //We sure that we don't have this maturity period in the DB
        MortgageCheckDto dto = new MortgageCheckDto(80000.0, 130000, 150000.0, 200000.0);
        var response = om.readValue(
                restMortgageRateMockMvc
                        .perform(post(INTEREST_CHECK_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dto)))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ErrorResponse.class);
        assertEquals("Mortgage data could not be found.", response.error());
        assertThat(response.errors()).isEmpty();

    }
}