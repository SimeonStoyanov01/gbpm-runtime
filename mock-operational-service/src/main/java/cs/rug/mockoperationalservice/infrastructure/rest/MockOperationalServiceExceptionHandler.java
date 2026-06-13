package cs.rug.mockoperationalservice.infrastructure.rest;

import cs.rug.mockoperationalservice.api.exceptions.ExecutionRunNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class MockOperationalServiceExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException exception) {
        List<String> errors = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String field = error instanceof FieldError fieldError
                            ? fieldError.getField()
                            : error.getObjectName();
                    return field + ": " + error.getDefaultMessage();
                })
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(String.join("\n", errors));
    }

    @ExceptionHandler(ExecutionRunNotFoundException.class)
    public ResponseEntity<String> handleExecutionRunNotFound(ExecutionRunNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedException(Exception exception) {
        log.error("Unexpected error occurred.", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
}
