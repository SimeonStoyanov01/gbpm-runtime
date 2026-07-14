package cs.rug.observationservice.infrastructure.messaging.inbound.engineevents;

import cs.rug.observationservice.api.events.enginetaskcompleted.EngineTaskCompletedEvent;
import cs.rug.observationservice.api.operations.createkeiobservation.CreateKeiObservationOperation;
import cs.rug.observationservice.api.operations.createkeiobservation.CreateKeiObservationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class EngineTaskCompletedEventListener {

    private final ObjectMapper objectMapper;
    private final CreateKeiObservationOperation createKeiObservationOperation;

    @RabbitListener(queues = "${runtime.observation.messaging.engine-events.queue-name}")
    public void handle(byte[] payload) {
        EngineTaskCompletedEvent event;
        try {
            event = readEvent(payload);
        } catch (IllegalArgumentException exception) {
            log.warn("Skipping invalid engine task completed event: {}", exception.getMessage());
            return;
        }

        log.info(
                "Received engine task completed event: processDefinitionKey={}, bpmnElementId={}",
                event.getExecution().getProcessDefinitionKey(),
                event.getExecution().getBpmnElementId()
        );
        createKeiObservationOperation.process(CreateKeiObservationRequest
                .builder()
                .execution(event.getExecution())
                .resourceUsages(event.getResourceUsages())
                .build());
    }

    private EngineTaskCompletedEvent readEvent(byte[] payload) {
        try {
            return objectMapper.readValue(payload, EngineTaskCompletedEvent.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Failed to deserialize engine task completed event.", exception);
        }
    }
}
