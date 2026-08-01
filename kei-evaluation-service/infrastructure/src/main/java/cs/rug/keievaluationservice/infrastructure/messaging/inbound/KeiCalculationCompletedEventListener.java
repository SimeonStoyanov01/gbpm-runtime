package cs.rug.keievaluationservice.infrastructure.messaging.inbound;

import cs.rug.keievaluationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.keievaluationservice.api.operations.evaluatekei.EvaluateKeiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeiCalculationCompletedEventListener {

    private final ObjectMapper objectMapper;
    private final EvaluateKeiOperation evaluateKeiOperation;

    @RabbitListener(queues = "${runtime.messaging.calculation-result.queue-name}")
    public void handle(byte[] payload) {
        KeiCalculationCompletedEvent event;
        try {
            event = readEvent(payload);
        } catch (IllegalArgumentException exception) {
            log.warn("Skipping invalid KEI calculation completed event: {}", exception.getMessage());
            return;
        }

        log.info(
                "Received KEI calculation completed event: eventId={}",
                event.getEventId()
        );

        evaluateKeiOperation.process(event);
    }

    private KeiCalculationCompletedEvent readEvent(byte[] payload) {
        KeiCalculationCompletedEvent event;
        try {
            event = objectMapper.readValue(payload, KeiCalculationCompletedEvent.class);
        } catch (JacksonException exception) {
            throw new IllegalArgumentException("Failed to deserialize KEI calculation completed event.", exception);
        }

        if (event == null) {
            throw new IllegalArgumentException("KEI calculation completed event must not be null.");
        }
        return event;
    }
}
