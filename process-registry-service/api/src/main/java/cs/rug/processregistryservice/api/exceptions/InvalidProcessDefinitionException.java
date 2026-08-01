package cs.rug.processregistryservice.api.exceptions;

public class InvalidProcessDefinitionException extends RuntimeException {

    public InvalidProcessDefinitionException() {
        super("Process definition is invalid.");
    }

    public InvalidProcessDefinitionException(Throwable cause) {
        super("Process definition is invalid.", cause);
    }
}
