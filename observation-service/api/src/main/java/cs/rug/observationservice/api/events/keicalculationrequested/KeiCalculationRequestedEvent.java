package cs.rug.observationservice.api.events.keicalculationrequested;

import cs.rug.observationservice.api.model.EngineExecutionContext;
import cs.rug.observationservice.api.model.KeiAnnotation;
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
    private Instant occurredAt;

    private CalculationRequestDescriptor calculation;
    private KeiAnnotation kei;
    private EngineExecutionContext execution;
    private CalculationRequestInputs inputs;
}
