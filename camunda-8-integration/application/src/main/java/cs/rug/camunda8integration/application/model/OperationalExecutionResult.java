package cs.rug.camunda8integration.application.model;

import cs.rug.camunda8integration.api.events.enginetaskcompleted.ResourceUsageFact;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OperationalExecutionResult {
    private String runId;
    private String status;
    private List<ResourceUsageFact> resourceUsages;
}
