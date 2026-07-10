package cs.rug.camunda8workerservice.event;

import cs.rug.camunda8workerservice.model.EngineExecutionContext;
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
public class EngineTaskCompletedEvent {
    private String eventId;
    private String eventType;
    private Instant occurredAt;
    private EngineExecutionContext execution;
    private String taskStatus;
    private List<ResourceUsageFact> resourceUsages;
}
