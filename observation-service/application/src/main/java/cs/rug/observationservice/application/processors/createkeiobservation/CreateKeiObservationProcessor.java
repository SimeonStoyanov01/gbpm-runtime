package cs.rug.observationservice.application.processors.createkeiobservation;

import cs.rug.observationservice.api.events.keicalculationrequested.KeiCalculationRequestedEvent;
import cs.rug.observationservice.api.model.KeiAnnotation;
import cs.rug.observationservice.api.model.ResourceUsage;
import cs.rug.observationservice.api.operations.createkeiobservation.CreateKeiObservationOperation;
import cs.rug.observationservice.api.operations.createkeiobservation.CreateKeiObservationRequest;
import cs.rug.observationservice.application.factory.KeiCalculationRequestedEventFactory;
import cs.rug.observationservice.application.out.KeiAnnotationLookupClient;
import cs.rug.observationservice.application.out.KeiCalculationRequestedEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateKeiObservationProcessor implements CreateKeiObservationOperation {

    private final KeiAnnotationLookupClient keiAnnotationLookupClient;
    private final KeiCalculationRequestedEventPublisher keiCalculationRequestedEventPublisher;
    private final KeiCalculationRequestedEventFactory keiCalculationRequestedEventFactory;

    @Override
    public void process(CreateKeiObservationRequest request) {
        List<KeiAnnotation> keiAnnotations = findKeiAnnotations(request);
        if (keiAnnotations.isEmpty()) {
            log.info(
                    "Engine task completed event has no KEI annotations: processDefinitionKey={}, bpmnElementId={}",
                    request.getExecution().getProcessDefinitionKey(),
                    request.getExecution().getBpmnElementId()
            );
            return;
        }

        log.info(
                "Observed KEI annotations for engine task: processDefinitionKey={}, bpmnElementId={}, keiAnnotationCount={}",
                request.getExecution().getProcessDefinitionKey(),
                request.getExecution().getBpmnElementId(),
                keiAnnotations.size()
        );

        requestCalculations(request, keiAnnotations);
    }

    private List<KeiAnnotation> findKeiAnnotations(CreateKeiObservationRequest request) {
        return keiAnnotationLookupClient.findKeiAnnotations(
                request.getExecution().getProcessDefinitionKey(),
                request.getExecution().getBpmnElementId()
        );
    }

    private void requestCalculations(
            CreateKeiObservationRequest request,
            List<KeiAnnotation> keiAnnotations
    ) {
        List<ResourceUsage> resourceUsages = request.getResourceUsages();
        if (resourceUsages == null || resourceUsages.isEmpty()) {
            log.info(
                    "Skipping KEI calculation request because resource usage data is missing: bpmnElementId={}",
                    request.getExecution().getBpmnElementId()
            );
            return;
        }

        for (KeiAnnotation keiAnnotation : keiAnnotations) {
            KeiCalculationRequestedEvent event = keiCalculationRequestedEventFactory.create(
                    request.getExecution(),
                    keiAnnotation,
                    resourceUsages
            );

            keiCalculationRequestedEventPublisher.publish(event);

            log.info(
                    "Published KEI calculation request event: keiId={}, resourceUsageCount={}",
                    keiAnnotation.getId(),
                    resourceUsages.size()
            );
        }
    }
}
