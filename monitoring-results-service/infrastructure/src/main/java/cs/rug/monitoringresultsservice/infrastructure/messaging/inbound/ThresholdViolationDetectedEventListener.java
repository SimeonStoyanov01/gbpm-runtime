package cs.rug.monitoringresultsservice.infrastructure.messaging.inbound;

import cs.rug.monitoringresultsservice.api.events.thresholdviolationdetected.ThresholdViolationDetectedEvent;
import cs.rug.monitoringresultsservice.api.operations.recordviolation.RecordViolationOperation;
import cs.rug.monitoringresultsservice.infrastructure.messaging.mapper.ThresholdViolationEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class ThresholdViolationDetectedEventListener {

    private final ObjectMapper objectMapper;
    private final ThresholdViolationEventMapper thresholdViolationEventMapper;
    private final RecordViolationOperation recordViolationOperation;

    @RabbitListener(queues = "${gbpmruntime.messaging.threshold-violation.queue-name}")
    public void handle(byte[] payload) {
        ThresholdViolationDetectedEvent event;
        try {
            event = readEvent(payload);
        } catch (IllegalArgumentException exception) {
            log.warn("Skipping invalid threshold violation detected event: {}", exception.getMessage());
            return;
        }

        log.info("Received threshold violation detected event: eventId={}", event.getEventId());
        recordViolationOperation.process(thresholdViolationEventMapper.toRequest(event));
    }

    private ThresholdViolationDetectedEvent readEvent(byte[] payload) {
        try {
            return objectMapper.readValue(payload, ThresholdViolationDetectedEvent.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Failed to deserialize threshold violation detected event.", exception);
        }
    }
}
