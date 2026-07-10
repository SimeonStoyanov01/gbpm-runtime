package cs.rug.processregistryservice.api.exceptions;

public class ProcessDefinitionNotRegisteredException extends RuntimeException {

    public ProcessDefinitionNotRegisteredException() {
        super("Process definition is not registered.");
    }
}
