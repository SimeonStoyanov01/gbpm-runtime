package cs.rug.gbpmruntime.observation.application.processors.createkeiobservation;

import cs.rug.gbpmruntime.observation.api.events.EngineTaskCompletedEvent;
import cs.rug.gbpmruntime.observation.api.events.BpmnContext;
import cs.rug.gbpmruntime.observation.api.events.EngineContext;
import cs.rug.gbpmruntime.observation.api.events.KeiAnnotationPayload;
import cs.rug.gbpmruntime.observation.api.events.KeiObservationEvent;
import cs.rug.gbpmruntime.observation.api.operations.publishkeiobservation.PublishKeiObservationEventOperation;
import cs.rug.gbpmruntime.observation.api.operations.publishkeiobservation.PublishKeiObservationEventRequest;
import cs.rug.gbpmruntime.processregistry.api.model.KeiAnnotationModel;
import cs.rug.gbpmruntime.processregistry.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateKeiObservationProcessor {

    private static final String EVENT_TYPE = "KEI_OBSERVATION_CREATED";
    private static final String CONTRACT_VERSION = "1.0";

    private final FindActivityKeiAnnotationsOperation findActivityKeiAnnotationsOperation;
    private final PublishKeiObservationEventOperation publishKeiObservationEventOperation;

    public void process(EngineTaskCompletedEvent sourceEvent) {
        List<KeiAnnotationPayload> keiAnnotations = findKeiAnnotations(sourceEvent);
        if (keiAnnotations.isEmpty()) {
            log.info(
                    "Engine task completed event has no KEI annotations: sourceEventId={}, processDefinitionKey={}, bpmnElementId={}",
                    sourceEvent.getEventId(),
                    sourceEvent.getProcessDefinitionKey(),
                    sourceEvent.getBpmnElementId()
            );
            return;
        }

        KeiObservationEvent observationEvent = buildObservationEvent(sourceEvent, keiAnnotations);
        publishKeiObservationEventOperation.process(PublishKeiObservationEventRequest
                .builder()
                .event(observationEvent)
                .build());

        log.info(
                "Published KEI observation event: eventId={}, sourceEventId={}, processDefinitionKey={}, bpmnElementId={}, keiAnnotationCount={}",
                observationEvent.getEventId(),
                observationEvent.getSourceEventId(),
                sourceEvent.getProcessDefinitionKey(),
                sourceEvent.getBpmnElementId(),
                keiAnnotations.size()
        );
    }

    private List<KeiAnnotationPayload> findKeiAnnotations(EngineTaskCompletedEvent sourceEvent) {
        FindActivityKeiAnnotationsResponse response = findActivityKeiAnnotationsOperation.process(FindActivityKeiAnnotationsRequest
                .builder()
                .processDefinitionKey(sourceEvent.getProcessDefinitionKey())
                .bpmnElementId(sourceEvent.getBpmnElementId())
                .build());

        if (response.getBpmn4esKeiAnnotations() == null) {
            return List.of();
        }

        return response.getBpmn4esKeiAnnotations()
                .stream()
                .map(this::toPayload)
                .toList();
    }

    private KeiObservationEvent buildObservationEvent(
            EngineTaskCompletedEvent sourceEvent,
            List<KeiAnnotationPayload> keiAnnotations
    ) {
        return KeiObservationEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EVENT_TYPE)
                .contractVersion(CONTRACT_VERSION)
                .sourceEventId(sourceEvent.getEventId())
                .occurredAt(Instant.now())
                .engine(EngineContext
                        .builder()
                        .engineType(sourceEvent.getEngineType())
                        .processDefinitionKey(sourceEvent.getProcessDefinitionKey())
                        .bpmnProcessId(sourceEvent.getBpmnProcessId())
                        .processInstanceKey(sourceEvent.getProcessInstanceKey())
                        .elementInstanceKey(sourceEvent.getElementInstanceKey())
                        .jobKey(sourceEvent.getJobKey())
                        .jobType(sourceEvent.getJobType())
                        .workerName(sourceEvent.getWorkerName())
                        .build())
                .bpmn(BpmnContext
                        .builder()
                        .processId(sourceEvent.getBpmnProcessId())
                        .elementId(sourceEvent.getBpmnElementId())
                        .build())
                .variablesBefore(sourceEvent.getVariablesBefore())
                .businessOutput(sourceEvent.getBusinessOutput())
                .workerObservation(sourceEvent.getWorkerObservation())
                .keiAnnotations(keiAnnotations)
                .build();
    }

    private KeiAnnotationPayload toPayload(KeiAnnotationModel keiAnnotationModel) {
        return KeiAnnotationPayload
                .builder()
                .id(keiAnnotationModel.getId())
                .unit(keiAnnotationModel.getUnit())
                .targetValue(keiAnnotationModel.getTargetValue())
                .icon(keiAnnotationModel.getIcon())
                .build();
    }
}
