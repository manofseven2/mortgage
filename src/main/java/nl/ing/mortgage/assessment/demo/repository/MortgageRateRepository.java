package nl.ing.mortgage.assessment.demo.repository;

import nl.ing.mortgage.assessment.demo.domain.MortgageRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

/**
 * Spring Data JPA repository for MortgageRate entity.
 */
@Repository
public interface MortgageRateRepository extends JpaRepository<MortgageRate, Long>, JpaSpecificationExecutor<MortgageRate> {
    Optional<MortgageRate> findTopByMaturityPeriodOrderByLastUpdateDesc(Integer maturityPeriod);
    @Query(value = """
        SELECT *
        FROM mortgage_rate r1
        WHERE r1.id = (
            SELECT r2.id
            FROM mortgage_rate r2
            WHERE r2.maturity_period = r1.maturity_period
            ORDER BY r2.last_update DESC, r2.id DESC
            LIMIT 1
        )
    """, nativeQuery = true)
    Page<MortgageRate> findAllCurrentMortgageRates(Pageable pageable);
}
