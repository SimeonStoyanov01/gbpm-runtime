package cs.rug.co2calculationservice.api.operations.calculateco2;

import cs.rug.co2calculationservice.api.base.ProcessorRequest;
import cs.rug.co2calculationservice.api.model.CalculationDescriptor;
import cs.rug.co2calculationservice.api.model.CalculationInputs;
import cs.rug.co2calculationservice.api.model.EngineExecutionContext;
import cs.rug.co2calculationservice.api.model.KeiAnnotation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculateCo2Request implements ProcessorRequest {
    @Valid
    @NotNull
    private CalculationDescriptor calculation;

    @Valid
    @NotNull
    private KeiAnnotation kei;

    @Valid
    @NotNull
    private EngineExecutionContext execution;

    @Valid
    @NotNull
    private CalculationInputs inputs;
}
