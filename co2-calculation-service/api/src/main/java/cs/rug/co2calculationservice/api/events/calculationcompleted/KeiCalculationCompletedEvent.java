package cs.rug.co2calculationservice.api.events.calculationcompleted;

import cs.rug.co2calculationservice.api.model.CalculationDescriptor;
import cs.rug.co2calculationservice.api.model.CalculationResult;
import cs.rug.co2calculationservice.api.model.EngineExecutionContext;
import cs.rug.co2calculationservice.api.model.KeiAnnotation;
import cs.rug.co2calculationservice.api.model.ResourceBreakdown;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeiCalculationCompletedEvent {
    private String eventId;
    private String eventType;
    private Instant occurredAt;
    private CalculationDescriptor calculation;
    private KeiAnnotation kei;
    private EngineExecutionContext execution;
    private CalculationResult result;
    private List<ResourceBreakdown> resourceBreakdown;
}
