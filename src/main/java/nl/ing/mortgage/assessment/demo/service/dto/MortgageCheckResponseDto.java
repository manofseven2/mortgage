package nl.ing.mortgage.assessment.demo.service.dto;

/**
 * Response entity when all criteria are meet
 * @param feasible
 * @param monthlyCost
 */
public record MortgageCheckResponseDto(boolean feasible, Double monthlyCost) {
}
