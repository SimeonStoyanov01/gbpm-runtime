package cs.rug.co2calculationservice.api.operations.calculateco2;

import cs.rug.co2calculationservice.api.base.ProcessorRequest;
import cs.rug.co2calculationservice.api.model.CalculationDescriptor;
import cs.rug.co2calculationservice.api.model.CalculationInputs;
import cs.rug.co2calculationservice.api.model.EngineExecutionContext;
import cs.rug.co2calculationservice.api.model.Kei;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculateCo2Request implements ProcessorRequest {
    private String calculationRequestId;
    private String observationId;
    private String sourceEventId;
    private CalculationDescriptor calculation;
    private Kei kei;
    private EngineExecutionContext execution;
    private CalculationInputs inputs;
}
