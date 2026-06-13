package cs.rug.mockoperationalservice.api.exceptions;

public class ExecutionRunNotFoundException extends RuntimeException {

    public ExecutionRunNotFoundException(String message) {
        super(message);
    }
}
