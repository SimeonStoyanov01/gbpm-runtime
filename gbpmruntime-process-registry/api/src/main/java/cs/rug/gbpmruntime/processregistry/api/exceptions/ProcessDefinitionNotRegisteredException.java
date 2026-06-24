package cs.rug.gbpmruntime.processregistry.api.exceptions;

public class ProcessDefinitionNotRegisteredException extends RuntimeException {

    public ProcessDefinitionNotRegisteredException() {
        super("Process definition is not registered.");
    }
}
