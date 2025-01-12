package nl.ing.mortgage.assessment.demo.service.impl;

import lombok.AllArgsConstructor;
import nl.ing.mortgage.assessment.demo.domain.MortgageRate;
import nl.ing.mortgage.assessment.demo.repository.MortgageRateRepository;
import nl.ing.mortgage.assessment.demo.service.MortgageRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class MortgageRateServiceImpl implements MortgageRateService {
    private static final Logger LOG = LoggerFactory.getLogger(MortgageRateServiceImpl.class);

    private final MortgageRateRepository mortgageRateRepository;


    @Override
    public MortgageRate save(MortgageRate mortgageRate) {
        LOG.debug("Request to save MortgageRate : {}", mortgageRate);
        return mortgageRateRepository.save(mortgageRate);
    }

    @Override
    public Optional<MortgageRate> findOne(Long id) {
        LOG.debug("Request to get MortgageRate : {}", id);
        return mortgageRateRepository.findById(id);
    }

    @Override
    @Cacheable("mortgageRateCache")
    public Page<MortgageRate> findAll(Pageable pageable) {
        LOG.debug("Request to get all MortgageRate. page: {}", pageable);
        return mortgageRateRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "mortgageRateCache", key = "#maturityPeriod")
    public Optional<MortgageRate> getLatestMortgageRateByMaturityPeriod(int maturityPeriod) {
        // Check cache first (this will be managed by @Cacheable)
        return mortgageRateRepository.findTopByMaturityPeriodOrderByLastUpdateDesc(maturityPeriod);
    }

    @Override
    public Page<MortgageRate> findAllCurrentMortgageRates(Pageable pageable) {
        LOG.debug("Request to get all current MortgageRate. page: {}", pageable);
        return mortgageRateRepository.findAllCurrentMortgageRates(pageable);
    }
}
