package cs.rug.mockoperationalservice.infrastructure.rest;

import cs.rug.mockoperationalservice.api.exceptions.ExecutionRunNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.ErrorResponse;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException exception) {
        String detail = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String field = error instanceof FieldError fieldError
                            ? fieldError.getField()
                            : error.getObjectName();
                    return field + ": " + error.getDefaultMessage();
                })
                .sorted()
                .collect(Collectors.joining("; "));

        return problem(HttpStatus.BAD_REQUEST, "Request validation failed", detail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleUnreadableRequest() {
        return problem(HttpStatus.BAD_REQUEST, "Malformed request", "The request body could not be read.");
    }

    @ExceptionHandler(ExecutionRunNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleExecutionRunNotFound(ExecutionRunNotFoundException exception) {
        return problem(HttpStatus.NOT_FOUND, "Execution run not found", exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedException(Exception exception) {
        if (exception instanceof ErrorResponse errorResponse) {
            return ResponseEntity
                    .status(errorResponse.getStatusCode())
                    .body(errorResponse.getBody());
        }

        log.error("Unexpected REST request failure.", exception);
        return problem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "The request could not be completed."
        );
    }

    private ResponseEntity<ProblemDetail> problem(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        return ResponseEntity.status(status).body(problem);
    }
}
