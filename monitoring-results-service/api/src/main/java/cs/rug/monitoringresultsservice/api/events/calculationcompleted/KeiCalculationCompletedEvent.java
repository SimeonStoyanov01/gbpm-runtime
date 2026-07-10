package cs.rug.monitoringresultsservice.api.events.calculationcompleted;

import cs.rug.monitoringresultsservice.api.model.CalculationDescriptor;
import cs.rug.monitoringresultsservice.api.model.CalculationResult;
import cs.rug.monitoringresultsservice.api.model.EngineExecutionContext;
import cs.rug.monitoringresultsservice.api.model.KeiAnnotation;
import cs.rug.monitoringresultsservice.api.model.ResourceBreakdown;
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
    private String contractVersion;
    private Instant occurredAt;
    private CalculationDescriptor calculation;
    private KeiAnnotation kei;
    private EngineExecutionContext execution;
    private CalculationResult result;
    private List<ResourceBreakdown> resourceBreakdown;
}
