package cs.rug.observationservice.api.operations.createkeiobservation;

import cs.rug.observationservice.api.model.EngineExecutionContext;
import cs.rug.observationservice.api.model.ResourceUsage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateKeiObservationRequest {
    private EngineExecutionContext execution;
    private List<ResourceUsage> resourceUsages;
}
