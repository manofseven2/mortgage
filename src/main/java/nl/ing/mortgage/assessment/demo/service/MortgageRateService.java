package nl.ing.mortgage.assessment.demo.service;

import nl.ing.mortgage.assessment.demo.domain.MortgageRate;
import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckDto;
import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link nl.ing.mortgage.assessment.demo.domain.MortgageRate}.
 */
public interface MortgageRateService {
    /**
     * Save a mortgageRate.
     *
     * @param mortgageRate the entity to save.
     * @return the persisted entity.
     */
    MortgageRate save(MortgageRate mortgageRate);

    /**
     * Get the "id" mortgageRate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MortgageRate> findOne(Long id);

    /**
     * Get all mortgage rates by pagination
     * @param pageable
     * @return a Page of results
     */
    Page<MortgageRate> findAll(Pageable pageable);

    /**
     * Get latest MortgageRate with maturityPeriod
     * @param maturityPeriod
     * @return the latest entity
     */
    Optional<MortgageRate> getLatestMortgageRateByMaturityPeriod(int maturityPeriod);

    /**
     * get list of latest interest rates
     * @param pageable requested page info
     * @return data for latest mortgage info
     */
    Page<MortgageRate> findAllCurrentMortgageRates(Pageable pageable);

}
