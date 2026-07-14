package cs.rug.monitoringresultsservice.infrastructure.messaging.inbound;

import cs.rug.monitoringresultsservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.monitoringresultsservice.api.operations.recordcalculation.RecordCalculationOperation;
import cs.rug.monitoringresultsservice.infrastructure.messaging.mapper.CalculationCompletedEventMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeiCalculationCompletedEventListener {

    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final CalculationCompletedEventMapper calculationCompletedEventMapper;
    private final RecordCalculationOperation recordCalculationOperation;

    @RabbitListener(queues = "${runtime.messaging.calculation-result.queue-name}")
    public void handle(byte[] payload) {
        KeiCalculationCompletedEvent event;
        try {
            event = readEvent(payload);
        } catch (IllegalArgumentException exception) {
            log.warn("Skipping invalid KEI calculation completed event: {}", exception.getMessage());
            return;
        }

        log.info(
                "Received KEI calculation completed event: eventId={}",
                event.getEventId()
        );
        if (hasTargetValue(event)) {
            log.info(
                    "Skipping calculation monitoring projection because target value is present: eventId={}, keiId={}",
                    event.getEventId(),
                    event.getKei().getId()
            );
            return;
        }

        recordCalculationOperation.process(calculationCompletedEventMapper.toMonitoringRecord(event));
    }

    private KeiCalculationCompletedEvent readEvent(byte[] payload) {
        KeiCalculationCompletedEvent event;
        try {
            event = objectMapper.readValue(payload, KeiCalculationCompletedEvent.class);
        } catch (JacksonException exception) {
            throw new IllegalArgumentException("Failed to deserialize KEI calculation completed event.", exception);
        }

        validate(event);
        return event;
    }

    private void validate(KeiCalculationCompletedEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("KEI calculation completed event must not be null.");
        }

        Set<ConstraintViolation<KeiCalculationCompletedEvent>> violations = validator.validate(event);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations
                    .stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .sorted()
                    .collect(Collectors.joining("; ")));
        }
    }

    private boolean hasTargetValue(KeiCalculationCompletedEvent event) {
        return event.getKei().getTargetValue() != null && !event.getKei().getTargetValue().isBlank();
    }
}
