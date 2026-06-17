package cs.rug.monitoringresultsservice.infrastructure.messaging.inbound;

import cs.rug.monitoringresultsservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.monitoringresultsservice.api.operations.recordcalculation.RecordCalculationOperation;
import cs.rug.monitoringresultsservice.infrastructure.messaging.mapper.CalculationCompletedEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeiCalculationCompletedEventListener {

    private final ObjectMapper objectMapper;
    private final CalculationCompletedEventMapper calculationCompletedEventMapper;
    private final RecordCalculationOperation recordCalculationOperation;

    @RabbitListener(queues = "${gbpmruntime.messaging.calculation-result.queue-name}")
    public void handle(byte[] payload) {
        KeiCalculationCompletedEvent event;
        try {
            event = readEvent(payload);
        } catch (IllegalArgumentException exception) {
            log.warn("Skipping invalid KEI calculation completed event: {}", exception.getMessage());
            return;
        }

        log.info("Received KEI calculation completed event: eventId={}", event.getEventId());
        recordCalculationOperation.process(calculationCompletedEventMapper.toRequest(event));
    }

    private KeiCalculationCompletedEvent readEvent(byte[] payload) {
        try {
            return objectMapper.readValue(payload, KeiCalculationCompletedEvent.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Failed to deserialize KEI calculation completed event.", exception);
        }
    }
}
