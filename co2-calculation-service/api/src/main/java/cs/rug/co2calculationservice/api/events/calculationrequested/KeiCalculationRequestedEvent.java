package cs.rug.co2calculationservice.api.events.calculationrequested;

import cs.rug.co2calculationservice.api.model.CalculationInputs;
import cs.rug.co2calculationservice.api.model.EngineExecutionContext;
import cs.rug.co2calculationservice.api.model.KeiAnnotation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeiCalculationRequestedEvent {
    @Valid
    @NotNull
    private KeiAnnotation kei;

    @Valid
    @NotNull
    private EngineExecutionContext execution;

    @NotBlank
    private String workObjectType;

    @Valid
    @NotNull
    private CalculationInputs inputs;
}
