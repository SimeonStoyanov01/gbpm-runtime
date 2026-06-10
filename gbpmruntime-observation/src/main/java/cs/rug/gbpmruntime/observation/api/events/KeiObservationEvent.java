package cs.rug.gbpmruntime.observation.api.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeiObservationEvent {
    private String eventId;
    private String eventType;
    private String contractVersion;
    private String sourceEventId;
    private Instant occurredAt;
    private EngineContext engine;
    private BpmnContext bpmn;
    private Map<String, Object> variablesBefore;
    private Map<String, Object> businessOutput;
    private Map<String, Object> workerObservation;
    private List<KeiAnnotationPayload> keiAnnotations;
}
