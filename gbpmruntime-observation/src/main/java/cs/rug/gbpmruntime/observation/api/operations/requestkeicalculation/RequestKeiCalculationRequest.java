package cs.rug.gbpmruntime.observation.api.operations.requestkeicalculation;

import cs.rug.gbpmruntime.common.api.base.ProcessorRequest;
import cs.rug.gbpmruntime.observation.api.model.EngineExecutionContext;
import cs.rug.gbpmruntime.observation.api.model.KeiAnnotation;
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
public class RequestKeiCalculationRequest implements ProcessorRequest {
    private String observationId;
    private String sourceEventId;
    private EngineExecutionContext execution;
    private List<ResourceUsageFact> resourceUsages;
    private List<KeiAnnotation> keiAnnotations;
}
