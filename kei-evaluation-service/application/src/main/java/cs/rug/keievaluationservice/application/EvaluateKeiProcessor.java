package cs.rug.keievaluationservice.application;

import cs.rug.keievaluationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.keievaluationservice.api.events.evaluationcompleted.KeiEvaluationCompletedEvent;
import cs.rug.keievaluationservice.api.model.EvaluationDetails;
import cs.rug.keievaluationservice.api.operations.evaluatekei.EvaluateKeiOperation;
import cs.rug.keievaluationservice.application.enums.EvaluationStatus;
import cs.rug.keievaluationservice.application.out.EvaluationEventPublisher;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluateKeiProcessor implements EvaluateKeiOperation {

    private final EvaluationEventPublisher evaluationEventPublisher;
    private final Validator validator;

    @Override
    public void process(KeiCalculationCompletedEvent event) {
        Optional<String> validationError = validate(event);
        if (validationError.isPresent()) {
            log.warn("Skipping invalid KEI calculation completed event: {}", validationError.get());
            return;
        }

        log.info(
                "Received calculation result for evaluation: calculationEventId={}, keiId={}",
                event.getEventId(),
                event.getKei().getId()
        );

        Optional<BigDecimal> targetValue = parseTargetValue(event.getKei().getTargetValue());
        if (targetValue.isEmpty()) {
            log.info(
                    "Skipping KEI evaluation because no numeric target is available: calculationEventId={}, keiId={}",
                    event.getEventId(),
                    event.getKei().getId()
            );
            return;
        }

        KeiEvaluationCompletedEvent evaluationEvent = createEvaluationEvent(event, targetValue.get());
        evaluationEventPublisher.publishEvaluation(evaluationEvent);
    }

    private KeiEvaluationCompletedEvent createEvaluationEvent(
            KeiCalculationCompletedEvent event,
            BigDecimal target
    ) {
        BigDecimal calculatedValue = event.getResult().getValue();
        String status = calculatedValue.compareTo(target) <= 0
                ? EvaluationStatus.WITHIN_TARGET.name()
                : EvaluationStatus.VIOLATED.name();

        return KeiEvaluationCompletedEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .occurredAt(Instant.now())
                .calculationEvent(event)
                .evaluation(EvaluationDetails
                        .builder()
                        .status(status)
                        .targetValue(target)
                        .difference(calculatedValue.subtract(target))
                        .build())
                .build();
    }

    private Optional<BigDecimal> parseTargetValue(String targetValue) {
        if (targetValue == null || targetValue.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(new BigDecimal(targetValue));
        } catch (NumberFormatException e) {
            log.warn("Skipping evaluation: targetValue is not a valid number: '{}'", targetValue);
            return Optional.empty();
        }
    }

    private Optional<String> validate(KeiCalculationCompletedEvent event) {
        Set<ConstraintViolation<KeiCalculationCompletedEvent>> violations = validator.validate(event);
        if (!violations.isEmpty()) {
            String message = violations
                    .stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining("; "));
            return Optional.of(message);
        }
        return Optional.empty();
    }
}
