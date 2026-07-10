package cs.rug.gbpmruntime.observation.api.events.keicalculationrequested;

import cs.rug.gbpmruntime.observation.api.model.EngineExecutionContext;
import cs.rug.gbpmruntime.observation.api.model.KeiAnnotation;
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

    private CalculationRequestDescriptor calculation;
    private KeiAnnotation kei;
    private EngineExecutionContext execution;
    private CalculationRequestInputs inputs;
}
