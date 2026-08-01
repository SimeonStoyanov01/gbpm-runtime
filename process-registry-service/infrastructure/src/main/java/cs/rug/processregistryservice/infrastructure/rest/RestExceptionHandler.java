package cs.rug.processregistryservice.infrastructure.rest;

import cs.rug.processregistryservice.api.exceptions.InvalidProcessDefinitionException;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class, TypeMismatchException.class})
    public ResponseEntity<ProblemDetail> handleValidation(Exception exception) {
        return problem(HttpStatus.BAD_REQUEST, "Request validation failed", exception.getMessage());
    }

    @ExceptionHandler(InvalidProcessDefinitionException.class)
    public ResponseEntity<ProblemDetail> handleInvalidProcessDefinition(InvalidProcessDefinitionException exception) {
        return problem(HttpStatus.BAD_REQUEST, "Invalid process definition", exception.getMessage());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ProblemDetail> handleDownstreamFailure(FeignException exception) {
        log.warn("Downstream service request failed with status {}.", exception.status(), exception);
        return problem(
                HttpStatus.BAD_GATEWAY,
                "Downstream service failure",
                "A required downstream service could not complete the request."
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(Exception exception) {
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
