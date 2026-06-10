package cs.rug.camunda8integration.api.exceptions;

public class EngineDeploymentException extends RuntimeException {

    public EngineDeploymentException(String message) {
        super(message);
    }

    public EngineDeploymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
