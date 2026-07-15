package cs.rug.observationservice.application.processors.createkeiobservation;

import cs.rug.observationservice.api.events.enginetaskcompleted.EngineTaskCompletedEvent;
import cs.rug.observationservice.api.events.keicalculationrequested.KeiCalculationRequestedEvent;
import cs.rug.observationservice.api.model.KeiAnnotation;
import cs.rug.observationservice.api.model.ResourceUsage;
import cs.rug.observationservice.api.operations.createkeiobservation.CreateKeiObservationOperation;
import cs.rug.observationservice.application.factory.KeiCalculationRequestedEventFactory;
import cs.rug.observationservice.application.out.KeiAnnotationLookupClient;
import cs.rug.observationservice.application.out.KeiCalculationRequestedEventPublisher;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateKeiObservationProcessor implements CreateKeiObservationOperation {

    private final KeiAnnotationLookupClient keiAnnotationLookupClient;
    private final KeiCalculationRequestedEventPublisher keiCalculationRequestedEventPublisher;
    private final KeiCalculationRequestedEventFactory keiCalculationRequestedEventFactory;
    private final Validator validator;

    @Override
    public void process(EngineTaskCompletedEvent event) {
        if (!isValid(event)) {
            return;
        }

        log.info(
                "Received engine task completed event: processDefinitionKey={}, bpmnElementId={}",
                event.getExecution().getProcessDefinitionKey(),
                event.getExecution().getBpmnElementId()
        );

        List<KeiAnnotation> keiAnnotations = findKeiAnnotations(event);
        if (keiAnnotations.isEmpty()) {
            log.info(
                    "Engine task completed event has no KEI annotations: processDefinitionKey={}, bpmnElementId={}",
                    event.getExecution().getProcessDefinitionKey(),
                    event.getExecution().getBpmnElementId()
            );
            return;
        }

        log.info(
                "Observed KEI annotations for engine task: processDefinitionKey={}, bpmnElementId={}, keiAnnotationCount={}",
                event.getExecution().getProcessDefinitionKey(),
                event.getExecution().getBpmnElementId(),
                keiAnnotations.size()
        );

        requestCalculations(event, keiAnnotations);
    }

    private List<KeiAnnotation> findKeiAnnotations(EngineTaskCompletedEvent event) {
        return keiAnnotationLookupClient.findKeiAnnotations(
                event.getExecution().getProcessDefinitionKey(),
                event.getExecution().getBpmnElementId()
        );
    }

    private void requestCalculations(
            EngineTaskCompletedEvent event,
            List<KeiAnnotation> keiAnnotations
    ) {
        List<ResourceUsage> resourceUsages = event.getResourceUsages();
        if (resourceUsages == null || resourceUsages.isEmpty()) {
            log.info(
                    "Skipping KEI calculation request because resource usage data is missing: bpmnElementId={}",
                    event.getExecution().getBpmnElementId()
            );
            return;
        }

        for (KeiAnnotation keiAnnotation : keiAnnotations) {
            KeiCalculationRequestedEvent calculationEvent = keiCalculationRequestedEventFactory.create(
                    event.getExecution(),
                    keiAnnotation,
                    resourceUsages
            );

            keiCalculationRequestedEventPublisher.publish(calculationEvent);

            log.info(
                    "Published KEI calculation request event: keiId={}, resourceUsageCount={}",
                    keiAnnotation.getId(),
                    resourceUsages.size()
            );
        }
    }

    private boolean isValid(EngineTaskCompletedEvent event) {
        Set<ConstraintViolation<EngineTaskCompletedEvent>> violations = validator.validate(event);
        if (violations.isEmpty()) {
            return true;
        }

        log.warn(
                "Skipping invalid engine task completed event: {}",
                violations
                        .stream()
                        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                        .sorted()
                        .collect(Collectors.joining("; "))
        );
        return false;
    }
}
