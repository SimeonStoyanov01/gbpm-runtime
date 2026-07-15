package cs.rug.observationservice.api.events.keicalculationrequested;

import cs.rug.observationservice.api.model.EngineExecutionContext;
import cs.rug.observationservice.api.model.KeiAnnotation;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeiCalculationRequestedEvent {
    private KeiAnnotation kei;
    private EngineExecutionContext execution;
    @NotBlank
    private String workObjectType;
    private CalculationInputs inputs;
}
