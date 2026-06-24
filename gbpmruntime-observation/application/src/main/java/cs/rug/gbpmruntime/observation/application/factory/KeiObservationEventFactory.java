package cs.rug.gbpmruntime.observation.application.factory;

import cs.rug.gbpmruntime.observation.api.events.keiobservationcreated.KeiObservationEvent;
import cs.rug.gbpmruntime.observation.api.model.KeiAnnotation;
import cs.rug.gbpmruntime.observation.api.operations.createkeiobservation.CreateKeiObservationRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class KeiObservationEventFactory {

    private static final String EVENT_TYPE = "KEI_OBSERVATION_CREATED";
    private static final String CONTRACT_VERSION = "1.0";

    public KeiObservationEvent create(
            CreateKeiObservationRequest request,
            List<KeiAnnotation> keiAnnotations
    ) {
        return KeiObservationEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EVENT_TYPE)
                .contractVersion(CONTRACT_VERSION)
                .sourceEventId(request.getSourceEventId())
                .occurredAt(Instant.now())
                .execution(request.getExecution())
                .taskStatus(request.getTaskStatus())
                .resourceUsages(request.getResourceUsages())
                .keiAnnotations(keiAnnotations)
                .build();
    }
}
