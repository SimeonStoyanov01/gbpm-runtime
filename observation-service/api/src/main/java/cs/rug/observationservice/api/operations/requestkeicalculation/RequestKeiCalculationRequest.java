package cs.rug.observationservice.api.operations.requestkeicalculation;

import cs.rug.observationservice.api.base.ProcessorRequest;
import cs.rug.observationservice.api.model.EngineExecutionContext;
import cs.rug.observationservice.api.model.KeiAnnotation;
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
public class RequestKeiCalculationRequest implements ProcessorRequest {
    private EngineExecutionContext execution;
    private List<ResourceUsageFact> resourceUsages;
    private List<KeiAnnotation> keiAnnotations;
}
