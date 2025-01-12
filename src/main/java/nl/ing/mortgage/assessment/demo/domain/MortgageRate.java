package nl.ing.mortgage.assessment.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.Instant;

/**
 * A MortgageRate.
 */
@Entity
@Table(name = "mortgage_rate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MortgageRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "maturity_period", nullable = false)
    private int maturityPeriod;

    @NotNull
    @Column(name = "interest_rate", nullable = false)
    private Double interestRate;

    @NotNull
    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate;
}
