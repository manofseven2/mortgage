package nl.ing.mortgage.assessment.demo.web.error;
import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String path,
        List<ValidationError> errors
) {}
