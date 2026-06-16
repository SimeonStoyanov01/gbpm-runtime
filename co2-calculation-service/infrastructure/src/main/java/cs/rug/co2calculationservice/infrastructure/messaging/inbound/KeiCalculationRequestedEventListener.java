package cs.rug.co2calculationservice.infrastructure.messaging.inbound;

import cs.rug.co2calculationservice.api.events.calculationrequested.KeiCalculationRequestedEvent;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeiCalculationRequestedEventListener {

    private final ObjectMapper objectMapper;
    private final CalculationRequestEventMapper calculationRequestEventMapper;
    private final CalculateCo2Operation calculateCo2Operation;

    @RabbitListener(queues = "${gbpmruntime.messaging.calculation-request.queue-name}")
    public void handle(byte[] payload) {
        KeiCalculationRequestedEvent event;
        try {
            event = readEvent(payload);
        } catch (IllegalArgumentException exception) {
            log.warn("Skipping invalid KEI calculation request event: {}", exception.getMessage());
            return;
        }

        log.info(
                "Received KEI calculation request: eventId={}, observationId={}, sourceEventId={}",
                event.getEventId(),
                event.getObservationId(),
                event.getSourceEventId()
        );

        calculateCo2Operation.process(calculationRequestEventMapper.toRequest(event));
    }

    private KeiCalculationRequestedEvent readEvent(byte[] payload) {
        try {
            return objectMapper.readValue(payload, KeiCalculationRequestedEvent.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Failed to deserialize KEI calculation requested event.", exception);
        }
    }
}
