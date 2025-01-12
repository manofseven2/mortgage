package nl.ing.mortgage.assessment.demo.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
    public BusinessException(String message, Throwable e) {
        super(message, e);
    }
}
