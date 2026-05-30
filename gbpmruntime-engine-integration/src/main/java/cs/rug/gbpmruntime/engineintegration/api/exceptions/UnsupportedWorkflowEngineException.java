package cs.rug.gbpmruntime.engineintegration.api.exceptions;

public class UnsupportedWorkflowEngineException extends RuntimeException {

    public UnsupportedWorkflowEngineException(String targetEngine) {
        super("Unsupported workflow engine: " + targetEngine);
    }
}
