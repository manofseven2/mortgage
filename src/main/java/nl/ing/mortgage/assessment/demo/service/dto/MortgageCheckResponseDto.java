package nl.ing.mortgage.assessment.demo.service.dto;

import java.math.BigDecimal;

public record MortgageCheckResponseDto(boolean feasible, Double monthlyCost) {
}
