package cs.rug.observationservice.api.events.keiobservationcreated;

import cs.rug.observationservice.api.model.EngineExecutionContext;
import cs.rug.observationservice.api.model.KeiAnnotation;
import cs.rug.observationservice.api.model.ResourceUsageFact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeiObservationEvent {
    private String eventId;
    private String eventType;
    private Instant occurredAt;

    private EngineExecutionContext execution;
    private String taskStatus;
    private List<ResourceUsageFact> resourceUsages;
    private List<KeiAnnotation> keiAnnotations;
}
