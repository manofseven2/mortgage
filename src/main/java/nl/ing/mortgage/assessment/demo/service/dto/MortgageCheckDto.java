package nl.ing.mortgage.assessment.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.validation.constraints.Min;
import nl.ing.mortgage.assessment.demo.service.validation.IncomeConstraint;
import nl.ing.mortgage.assessment.demo.service.validation.MortgageAmountConstraint;

import java.util.HashMap;
import java.util.Map;

/**
 * This DTO is used to send required information to check
 * @param income
 * @param maturityPeriod
 * @param loanValue
 * @param homeValue
 * @param additionalProperties
 */
@IncomeConstraint
@MortgageAmountConstraint
public record MortgageCheckDto(@Min(0) Double income, @Min(1) Integer maturityPeriod, Double loanValue,
                               Double homeValue, Map<String, Object> additionalProperties) {

    public MortgageCheckDto(Double income, Integer maturityPeriod, Double loanValue, Double homeValue) {
        this(income, maturityPeriod, loanValue, homeValue, new HashMap<>());
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
