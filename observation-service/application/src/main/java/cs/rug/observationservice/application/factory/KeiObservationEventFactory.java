package cs.rug.observationservice.application.factory;

import cs.rug.observationservice.api.events.keiobservationcreated.KeiObservationEvent;
import cs.rug.observationservice.api.model.KeiAnnotation;
import cs.rug.observationservice.api.operations.createkeiobservation.CreateKeiObservationRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class KeiObservationEventFactory {

    private static final String EVENT_TYPE = "KEI_OBSERVATION_CREATED";

    public KeiObservationEvent create(
            CreateKeiObservationRequest request,
            List<KeiAnnotation> keiAnnotations
    ) {
        return KeiObservationEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EVENT_TYPE)
                .occurredAt(Instant.now())
                .execution(request.getExecution())
                .taskStatus(request.getTaskStatus())
                .resourceUsages(request.getResourceUsages())
                .keiAnnotations(keiAnnotations)
                .build();
    }
}
