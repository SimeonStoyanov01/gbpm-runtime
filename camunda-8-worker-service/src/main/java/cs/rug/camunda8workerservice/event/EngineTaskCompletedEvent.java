package cs.rug.camunda8workerservice.event;

import cs.rug.camunda8workerservice.model.EngineExecutionContext;
import cs.rug.camunda8workerservice.model.ResourceUsage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineTaskCompletedEvent {
    private EngineExecutionContext execution;
    private List<ResourceUsage> resourceUsages;
}
