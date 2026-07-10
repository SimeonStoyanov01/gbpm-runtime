package cs.rug.observationservice.api.operations.createkeiobservation;

import cs.rug.observationservice.api.base.ProcessorRequest;
import cs.rug.observationservice.api.model.EngineExecutionContext;
import cs.rug.observationservice.api.model.ResourceUsageFact;
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
    private EngineExecutionContext execution;
    private String taskStatus;
    private List<ResourceUsageFact> resourceUsages;
}
