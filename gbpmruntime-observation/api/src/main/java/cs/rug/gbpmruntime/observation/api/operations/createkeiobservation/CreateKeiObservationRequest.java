package cs.rug.gbpmruntime.observation.api.operations.createkeiobservation;

import cs.rug.gbpmruntime.observation.api.base.ProcessorRequest;
import cs.rug.gbpmruntime.observation.api.model.EngineExecutionContext;
import cs.rug.gbpmruntime.observation.api.model.ResourceUsageFact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateKeiObservationRequest implements ProcessorRequest {
    private String sourceEventId;
    private EngineExecutionContext execution;
    private String taskStatus;
    private List<ResourceUsageFact> resourceUsages;
}
