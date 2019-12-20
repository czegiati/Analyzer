package exceptions;

public class AnalyzerAttributeException extends RuntimeException {
    public AnalyzerAttributeException(String message) {
        super(message);
    }

    public AnalyzerAttributeException(String message, Throwable cause) {
        super(message, cause);
    }
}
