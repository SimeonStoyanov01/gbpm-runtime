package cs.rug.gbpmruntime.processregistry.api.exceptions;

public class ProcessDefinitionNotReadyException extends RuntimeException {

    public ProcessDefinitionNotReadyException() {
        super("Process definition is not ready.");
    }
}
