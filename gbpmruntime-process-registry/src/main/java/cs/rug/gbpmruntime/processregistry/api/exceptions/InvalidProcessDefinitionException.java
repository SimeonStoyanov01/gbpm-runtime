package cs.rug.gbpmruntime.processregistry.api.exceptions;

public class InvalidProcessDefinitionException extends RuntimeException {

    public InvalidProcessDefinitionException() {
        super("Process definition is invalid.");
    }
}
