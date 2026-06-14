package cs.rug.gbpmruntime.observation.api.events.enginetaskcompleted;

import cs.rug.gbpmruntime.observation.api.model.EngineExecutionContext;
import cs.rug.gbpmruntime.observation.api.model.ResourceUsageFact;
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
    private String contractVersion;
    private Instant occurredAt;
    private EngineExecutionContext execution;
    private String taskStatus;
    private List<ResourceUsageFact> resourceUsages;
}
