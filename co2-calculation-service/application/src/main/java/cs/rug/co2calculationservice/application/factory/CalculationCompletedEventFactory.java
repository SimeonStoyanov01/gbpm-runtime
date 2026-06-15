package cs.rug.co2calculationservice.application.factory;

import cs.rug.co2calculationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.co2calculationservice.api.model.CalculationResult;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Request;
import cs.rug.co2calculationservice.application.model.CalculationOutcome;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class CalculationCompletedEventFactory {

    private static final String EVENT_TYPE = "KEI_CALCULATION_COMPLETED";
    private static final String CONTRACT_VERSION = "1.0";

    public KeiCalculationCompletedEvent create(
            CalculateCo2Request request,
            CalculationOutcome outcome
    ) {
        return KeiCalculationCompletedEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EVENT_TYPE)
                .contractVersion(CONTRACT_VERSION)
                .calculationRequestId(request.getCalculationRequestId())
                .observationId(request.getObservationId())
                .sourceEventId(request.getSourceEventId())
                .occurredAt(Instant.now())
                .calculation(request.getCalculation())
                .kei(request.getKei())
                .execution(request.getExecution())
                .result(CalculationResult
                        .builder()
                        .status(outcome.getStatus())
                        .value(outcome.getValue())
                        .unit(outcome.getUnit())
                        .build())
                .resourceBreakdown(outcome.getResourceBreakdown())
                .errors(outcome.getErrors())
                .build();
    }
}
