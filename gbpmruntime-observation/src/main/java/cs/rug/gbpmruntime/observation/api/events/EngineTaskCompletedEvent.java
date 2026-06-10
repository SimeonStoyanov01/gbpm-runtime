package cs.rug.gbpmruntime.observation.api.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineTaskCompletedEvent {
    private String eventId;
    private String eventType;
    private String engineType;
    private Long processDefinitionKey;
    private String bpmnProcessId;
    private Long processInstanceKey;
    private Long elementInstanceKey;
    private String bpmnElementId;
    private Long jobKey;
    private String jobType;
    private String workerName;
    private Instant occurredAt;
    private Map<String, Object> variablesBefore;
    private Map<String, Object> businessOutput;
    private Map<String, Object> workerObservation;
}
