package nl.ing.mortgage.assessment.demo.service;

import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckDto;
import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckResponseDto;

public interface MortgageServiceFacade {

    /**
     * Calculate mortgage
     *
     * @param dto mortgage data
     * @return calculated data about the mortgage
     */
    MortgageCheckResponseDto calculate(MortgageCheckDto dto);
}
