package hu.unideb.czty.analyzer.exceptions;

public class AnalyzerDetectorException extends RuntimeException {
    public AnalyzerDetectorException(String message) {
        super(message);
    }
    public AnalyzerDetectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
