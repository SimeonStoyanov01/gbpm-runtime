package cs.rug.co2calculationservice.infrastructure.messaging.inbound;

import cs.rug.co2calculationservice.api.events.calculationrequested.KeiCalculationRequestedEvent;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Request;
import org.springframework.stereotype.Component;

@Component
public class CalculationRequestEventMapper {

    public CalculateCo2Request toRequest(KeiCalculationRequestedEvent event) {
        return CalculateCo2Request
                .builder()
                .calculationRequestId(event.getEventId())
                .observationId(event.getObservationId())
                .sourceEventId(event.getSourceEventId())
                .calculation(event.getCalculation())
                .kei(event.getKei())
                .execution(event.getExecution())
                .inputs(event.getInputs())
                .build();
    }
}
