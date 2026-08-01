package cs.rug.monitoringresultsservice.application.validation;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventContractValidator {

    private final Validator validator;

    public <T> List<String> validate(T event) {
        return validator
                .validate(event)
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .sorted()
                .toList();
    }
}
