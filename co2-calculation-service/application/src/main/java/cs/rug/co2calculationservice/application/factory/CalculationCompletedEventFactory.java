package cs.rug.co2calculationservice.application.factory;

import cs.rug.co2calculationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.co2calculationservice.api.events.calculationrequested.KeiCalculationRequestedEvent;
import cs.rug.co2calculationservice.api.model.CalculationMetadata;
import cs.rug.co2calculationservice.api.model.CalculationResult;
import cs.rug.co2calculationservice.application.model.CalculationOutcome;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class CalculationCompletedEventFactory {

    public KeiCalculationCompletedEvent create(
            KeiCalculationRequestedEvent request,
            CalculationOutcome outcome,
            CalculationMetadata calculation
    ) {
        return KeiCalculationCompletedEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .occurredAt(Instant.now())
                .calculation(calculation)
                .kei(request.getKei())
                .execution(request.getExecution())
                .workObjectType(request.getWorkObjectType())
                .result(CalculationResult
                        .builder()
                        .value(outcome.getValue())
                        .unit(outcome.getUnit())
                        .build())
                .resourceBreakdown(outcome.getResourceBreakdown())
                .build();
    }
}
