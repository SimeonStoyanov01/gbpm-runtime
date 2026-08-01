package cs.rug.co2calculationservice.api.events.calculationfailed;

import cs.rug.co2calculationservice.api.model.CalculationMetadata;
import cs.rug.co2calculationservice.api.model.CalculationError;
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
public class KeiCalculationFailedEvent {
    private String eventId;
    private Instant occurredAt;
    private CalculationMetadata calculation;
    private KeiAnnotation kei;
    private EngineExecutionContext execution;
    private CalculationError error;
}
