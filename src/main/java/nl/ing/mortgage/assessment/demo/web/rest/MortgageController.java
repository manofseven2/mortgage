package nl.ing.mortgage.assessment.demo.web.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.ing.mortgage.assessment.demo.domain.MortgageRate;
import nl.ing.mortgage.assessment.demo.service.MortgageRateService;
import nl.ing.mortgage.assessment.demo.service.MortgageServiceFacade;
import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckDto;
import nl.ing.mortgage.assessment.demo.service.dto.MortgageCheckResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class MortgageController {
    private static final Logger LOG = LoggerFactory.getLogger(MortgageController.class);

    private final MortgageRateService mortgageRateService;
    private final MortgageServiceFacade mortgageServiceFacade;
    private static final String HEADER_X_TOTAL_COUNT = "X-Total-Count";

    @GetMapping("/interest-rates")
    public ResponseEntity<List<MortgageRate>> getAllCurrentRates(@ParameterObject Pageable pageable) {
        LOG.debug("request for interest rates.");
        Page<MortgageRate> page = mortgageRateService.findAllCurrentMortgageRates(pageable);
        HttpHeaders headers = generatePaginationHttpHeaders(page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @PostMapping("/mortgage-check")
    public ResponseEntity<MortgageCheckResponseDto> mortgageCheck(@Valid @RequestBody MortgageCheckDto dto){
        LOG.debug("Request for mortgage calculation: {}", dto);
        return ResponseEntity.ok(mortgageServiceFacade.calculate(dto));
    }

    public HttpHeaders generatePaginationHttpHeaders(Page<MortgageRate> page) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_X_TOTAL_COUNT, Long.toString(page.getTotalElements()));
        return headers;
    }

}
