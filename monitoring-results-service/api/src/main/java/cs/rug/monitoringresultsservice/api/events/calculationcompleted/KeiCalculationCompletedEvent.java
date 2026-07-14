package cs.rug.monitoringresultsservice.api.events.calculationcompleted;

import cs.rug.monitoringresultsservice.api.model.CalculationMetadata;
import cs.rug.monitoringresultsservice.api.model.CalculationResult;
import cs.rug.monitoringresultsservice.api.model.EngineExecutionContext;
import cs.rug.monitoringresultsservice.api.model.KeiAnnotation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeiCalculationCompletedEvent {
    private String eventId;
    private Instant occurredAt;
    private CalculationMetadata calculation;
    private KeiAnnotation kei;
    private EngineExecutionContext execution;
    private CalculationResult result;
}
