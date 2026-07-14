package cs.rug.co2calculationservice.application.factory;

import cs.rug.co2calculationservice.api.events.calculationfailed.KeiCalculationFailedEvent;
import cs.rug.co2calculationservice.api.events.calculationrequested.KeiCalculationRequestedEvent;
import cs.rug.co2calculationservice.api.model.CalculationMetadata;
import cs.rug.co2calculationservice.api.model.CalculationError;
import cs.rug.co2calculationservice.application.CalculationFailureException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class CalculationFailedEventFactory {

    public KeiCalculationFailedEvent create(
            KeiCalculationRequestedEvent request,
            CalculationFailureException exception,
            CalculationMetadata calculation
    ) {
        return KeiCalculationFailedEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .occurredAt(Instant.now())
                .calculation(calculation)
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
