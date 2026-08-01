package cs.rug.camunda8integration.api.exceptions;

public class EngineProcessStartException extends RuntimeException {

    public EngineProcessStartException(String message) {
        super(message);
    }

    public EngineProcessStartException(String message, Throwable cause) {
        super(message, cause);
    }
}
