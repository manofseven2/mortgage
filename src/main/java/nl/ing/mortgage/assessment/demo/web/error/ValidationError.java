package nl.ing.mortgage.assessment.demo.web.error;

public record ValidationError(
        String field,
        String message
) {}
