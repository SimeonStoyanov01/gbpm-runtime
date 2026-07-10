package cs.rug.observationservice.application.processors.createkeiobservation;

import cs.rug.observationservice.api.events.keiobservationcreated.KeiObservationEvent;
import cs.rug.observationservice.api.model.KeiAnnotation;
import cs.rug.observationservice.api.operations.createkeiobservation.CreateKeiObservationOperation;
import cs.rug.observationservice.api.operations.createkeiobservation.CreateKeiObservationRequest;
import cs.rug.observationservice.api.operations.requestkeicalculation.RequestKeiCalculationOperation;
import cs.rug.observationservice.api.operations.requestkeicalculation.RequestKeiCalculationRequest;
import cs.rug.observationservice.application.factory.KeiObservationEventFactory;
import cs.rug.observationservice.application.out.KeiAnnotationLookupClient;
import cs.rug.observationservice.application.out.KeiObservationEventPublisher;
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
                    "Engine task completed event has no KEI annotations: processDefinitionKey={}, bpmnElementId={}",
                    request.getExecution().getProcessDefinitionKey(),
                    request.getExecution().getBpmnElementId()
            );
            return;
        }

        KeiObservationEvent observationEvent = keiObservationEventFactory.create(request, keiAnnotations);
        keiObservationEventPublisher.publish(observationEvent);

        log.info(
                "Published KEI observation event: eventId={}, processDefinitionKey={}, bpmnElementId={}, keiAnnotationCount={}",
                observationEvent.getEventId(),
                request.getExecution().getProcessDefinitionKey(),
                request.getExecution().getBpmnElementId(),
                keiAnnotations.size()
        );

        requestKeiCalculationOperation.process(RequestKeiCalculationRequest
                .builder()
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
