package cs.rug.co2calculationservice.application.factory;

import cs.rug.co2calculationservice.api.events.calculationfailed.KeiCalculationFailedEvent;
import cs.rug.co2calculationservice.api.model.CalculationError;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Request;
import cs.rug.co2calculationservice.application.CalculationFailureException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class CalculationFailedEventFactory {

    private static final String EVENT_TYPE = "KEI_CALCULATION_FAILED";

    public KeiCalculationFailedEvent create(
            CalculateCo2Request request,
            CalculationFailureException exception
    ) {
        return KeiCalculationFailedEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EVENT_TYPE)
                .occurredAt(Instant.now())
                .calculation(request.getCalculation())
                .kei(request.getKei())
                .execution(request.getExecution())
                .error(CalculationError
                        .builder()
                        .code(exception.getCode())
                        .message(exception.getMessage())
                        .build())
                .build();
    }
}
