package cs.rug.keievaluationservice.application;

import cs.rug.keievaluationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.keievaluationservice.api.events.evaluationcompleted.KeiEvaluationCompletedEvent;
import cs.rug.keievaluationservice.api.events.thresholdviolationdetected.ThresholdViolationDetectedEvent;
import cs.rug.keievaluationservice.api.model.CalculatedResult;
import cs.rug.keievaluationservice.api.model.EvaluationDetails;
import cs.rug.keievaluationservice.api.operations.evaluatekei.EvaluateKeiOperation;
import cs.rug.keievaluationservice.api.operations.evaluatekei.EvaluateKeiRequest;
import cs.rug.keievaluationservice.api.operations.evaluatekei.EvaluateKeiResponse;
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

    private static final String EVENT_TYPE = "KEI_EVALUATION_COMPLETED";
    private static final String VIOLATION_EVENT_TYPE = "THRESHOLD_VIOLATION_DETECTED";
    private static final String CONTRACT_VERSION = "1.0";
    private static final String WITHIN_TARGET_STATUS = "WITHIN_TARGET";
    private static final String VIOLATED_STATUS = "VIOLATED";
    private static final String LESS_THAN_OR_EQUAL_OPERATOR = "LESS_THAN_OR_EQUAL";

    private final EvaluationEventPublisher evaluationEventPublisher;
    private final Validator validator;

    @Override
    public EvaluateKeiResponse process(EvaluateKeiRequest request) {
        Optional<String> validationError = validate(request);
        if (validationError.isPresent()) {
            log.warn("Skipping invalid KEI calculation completed event: {}", validationError.get());
            return EvaluateKeiResponse
                    .builder()
                    .evaluated(false)
                    .build();
        }

        KeiCalculationCompletedEvent event = request.getEvent();
        log.info(
                "Received calculation result for evaluation: calculationResultId={}, observationId={}, keiId={}",
                event.getEventId(),
                event.getObservationId(),
                event.getKei().getId()
        );

        Optional<BigDecimal> targetValue = parseTargetValue(event.getKei().getTargetValue());
        if (targetValue.isEmpty()) {
            log.info(
                    "Skipping KEI evaluation because no numeric target is available: calculationResultId={}, keiId={}",
                    event.getEventId(),
                    event.getKei().getId()
            );
            return EvaluateKeiResponse
                    .builder()
                    .calculationResultId(event.getEventId())
                    .evaluated(false)
                    .build();
        }

        KeiEvaluationCompletedEvent evaluationEvent = createEvaluationEvent(event, targetValue.get());
        evaluationEventPublisher.publishEvaluation(evaluationEvent);
        publishViolationIfNeeded(evaluationEvent);

        return EvaluateKeiResponse
                .builder()
                .calculationResultId(event.getEventId())
                .evaluated(true)
                .event(evaluationEvent)
                .build();
    }

    private void publishViolationIfNeeded(KeiEvaluationCompletedEvent event) {
        if (!VIOLATED_STATUS.equals(event.getEvaluation().getStatus())) {
            return;
        }

        evaluationEventPublisher.publishViolation(ThresholdViolationDetectedEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(VIOLATION_EVENT_TYPE)
                .contractVersion(CONTRACT_VERSION)
                .occurredAt(Instant.now())
                .processDefinitionKey(event.getExecution().getProcessDefinitionKey())
                .bpmnProcessId(event.getExecution().getBpmnProcessId())
                .serviceTaskId(event.getExecution().getBpmnElementId())
                .processInstanceKey(event.getExecution().getProcessInstanceKey())
                .emissionType(event.getKei().getId())
                .calculatedValue(event.getCalculatedResult().getValue())
                .targetValue(event.getEvaluation().getTargetValue())
                .difference(event.getEvaluation().getDifference())
                .status(event.getEvaluation().getStatus())
                .build());
    }

    private KeiEvaluationCompletedEvent createEvaluationEvent(
            KeiCalculationCompletedEvent event,
            BigDecimal target
    ) {
        BigDecimal calculatedValue = event.getResult().getValue();
        String status = calculatedValue.compareTo(target) <= 0 ? WITHIN_TARGET_STATUS : VIOLATED_STATUS;

        return KeiEvaluationCompletedEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EVENT_TYPE)
                .contractVersion(CONTRACT_VERSION)
                .calculationResultId(event.getEventId())
                .calculationRequestId(event.getCalculationRequestId())
                .observationId(event.getObservationId())
                .sourceEventId(event.getSourceEventId())
                .occurredAt(Instant.now())
                .calculation(event.getCalculation())
                .kei(event.getKei())
                .execution(event.getExecution())
                .calculatedResult(CalculatedResult
                        .builder()
                        .value(calculatedValue)
                        .unit(event.getResult().getUnit())
                        .build())
                .evaluation(EvaluationDetails
                        .builder()
                        .status(status)
                        .operator(LESS_THAN_OR_EQUAL_OPERATOR)
                        .targetValue(target)
                        .difference(calculatedValue.subtract(target))
                        .unit(event.getKei().getUnit())
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

    private Optional<String> validate(EvaluateKeiRequest request) {
        Set<ConstraintViolation<EvaluateKeiRequest>> violations = validator.validate(request);
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
