package cs.rug.gbpmruntime.observation.infrastructure.rabbitmq.engineevents;

import cs.rug.gbpmruntime.observation.api.events.EngineTaskCompletedEvent;
import cs.rug.gbpmruntime.observation.application.processors.createkeiobservation.CreateKeiObservationProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EngineTaskCompletedEventListener {

    private final CreateKeiObservationProcessor createKeiObservationProcessor;

    @RabbitListener(queues = "${gbpmruntime.observation.messaging.engine-events.queue-name}")
    public void handle(EngineTaskCompletedEvent event) {
        log.info(
                "Received engine task completed event: eventId={}, processDefinitionKey={}, bpmnElementId={}",
                event.getEventId(),
                event.getProcessDefinitionKey(),
                event.getBpmnElementId()
        );
        createKeiObservationProcessor.process(event);
    }
}
