package hu.unideb.czty.analyzer.exceptions;

public class AbstractObjectInstantiationFailureException extends RuntimeException {
    public AbstractObjectInstantiationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractObjectInstantiationFailureException(String message) {
        super(message);
    }
}
