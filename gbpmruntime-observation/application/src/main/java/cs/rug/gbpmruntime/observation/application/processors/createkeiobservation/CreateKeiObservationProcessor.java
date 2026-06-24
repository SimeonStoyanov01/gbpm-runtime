package cs.rug.gbpmruntime.observation.application.processors.createkeiobservation;

import cs.rug.gbpmruntime.observation.api.events.keiobservationcreated.KeiObservationEvent;
import cs.rug.gbpmruntime.observation.api.model.KeiAnnotation;
import cs.rug.gbpmruntime.observation.api.operations.createkeiobservation.CreateKeiObservationOperation;
import cs.rug.gbpmruntime.observation.api.operations.createkeiobservation.CreateKeiObservationRequest;
import cs.rug.gbpmruntime.observation.api.operations.requestkeicalculation.RequestKeiCalculationOperation;
import cs.rug.gbpmruntime.observation.api.operations.requestkeicalculation.RequestKeiCalculationRequest;
import cs.rug.gbpmruntime.observation.application.factory.KeiObservationEventFactory;
import cs.rug.gbpmruntime.observation.application.out.KeiAnnotationLookupClient;
import cs.rug.gbpmruntime.observation.application.out.KeiObservationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateKeiObservationProcessor implements CreateKeiObservationOperation {

    private final KeiAnnotationLookupClient keiAnnotationLookupClient;
    private final KeiObservationEventPublisher keiObservationEventPublisher;
    private final RequestKeiCalculationOperation requestKeiCalculationOperation;
    private final KeiObservationEventFactory keiObservationEventFactory;

    @Override
    public void process(CreateKeiObservationRequest request) {
        List<KeiAnnotation> keiAnnotations = findKeiAnnotations(request);
        if (keiAnnotations.isEmpty()) {
            log.info(
                    "Engine task completed event has no KEI annotations: sourceEventId={}, processDefinitionKey={}, bpmnElementId={}",
                    request.getSourceEventId(),
                    request.getExecution().getProcessDefinitionKey(),
                    request.getExecution().getBpmnElementId()
            );
            return;
        }

        KeiObservationEvent observationEvent = keiObservationEventFactory.create(request, keiAnnotations);
        keiObservationEventPublisher.publish(observationEvent);

        log.info(
                "Published KEI observation event: eventId={}, sourceEventId={}, processDefinitionKey={}, bpmnElementId={}, keiAnnotationCount={}",
                observationEvent.getEventId(),
                observationEvent.getSourceEventId(),
                request.getExecution().getProcessDefinitionKey(),
                request.getExecution().getBpmnElementId(),
                keiAnnotations.size()
        );

        requestKeiCalculationOperation.process(RequestKeiCalculationRequest
                .builder()
                .observationId(observationEvent.getEventId())
                .sourceEventId(observationEvent.getSourceEventId())
                .execution(observationEvent.getExecution())
                .resourceUsages(observationEvent.getResourceUsages())
                .keiAnnotations(observationEvent.getKeiAnnotations())
                .build());
    }

    private List<KeiAnnotation> findKeiAnnotations(CreateKeiObservationRequest request) {
        return keiAnnotationLookupClient.findKeiAnnotations(
                request.getExecution().getProcessDefinitionKey(),
                request.getExecution().getBpmnElementId()
        );
    }

}
