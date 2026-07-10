package cs.rug.co2calculationservice.api.events.calculationrequested;

import cs.rug.co2calculationservice.api.model.CalculationDescriptor;
import cs.rug.co2calculationservice.api.model.CalculationInputs;
import cs.rug.co2calculationservice.api.model.EngineExecutionContext;
import cs.rug.co2calculationservice.api.model.KeiAnnotation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeiCalculationRequestedEvent {
    private String eventId;
    private String eventType;
    private String contractVersion;
    private Instant occurredAt;
    private CalculationDescriptor calculation;
    private KeiAnnotation kei;
    private EngineExecutionContext execution;
    private CalculationInputs inputs;
}
