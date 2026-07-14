package cs.rug.monitoringresultsservice.infrastructure.messaging.inbound;

import cs.rug.monitoringresultsservice.api.events.evaluationcompleted.KeiEvaluationCompletedEvent;
import cs.rug.monitoringresultsservice.api.operations.recordevaluation.RecordEvaluationOperation;
import cs.rug.monitoringresultsservice.infrastructure.messaging.mapper.EvaluationCompletedEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeiEvaluationCompletedEventListener {

    private final ObjectMapper objectMapper;
    private final EvaluationCompletedEventMapper evaluationCompletedEventMapper;
    private final RecordEvaluationOperation recordEvaluationOperation;

    @RabbitListener(queues = "${runtime.messaging.evaluation-result.queue-name}")
    public void handle(byte[] payload) {
        KeiEvaluationCompletedEvent event;
        try {
            event = readEvent(payload);
        } catch (IllegalArgumentException exception) {
            log.warn("Skipping invalid KEI evaluation completed event: {}", exception.getMessage());
            return;
        }

        log.info(
                "Received KEI evaluation completed event: eventId={}",
                event.getEventId()
        );
        recordEvaluationOperation.process(evaluationCompletedEventMapper.toMonitoringRecord(event));
    }

    private KeiEvaluationCompletedEvent readEvent(byte[] payload) {
        try {
            return objectMapper.readValue(payload, KeiEvaluationCompletedEvent.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Failed to deserialize KEI evaluation completed event.", exception);
        }
    }
}
