package cs.rug.gbpmruntime.observation.infrastructure.messaging.inbound.engineevents;

import cs.rug.gbpmruntime.observation.api.events.enginetaskcompleted.EngineTaskCompletedEvent;
import cs.rug.gbpmruntime.observation.api.operations.createkeiobservation.CreateKeiObservationOperation;
import cs.rug.gbpmruntime.observation.api.operations.createkeiobservation.CreateKeiObservationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EngineTaskCompletedEventListener {

    private final CreateKeiObservationOperation createKeiObservationOperation;

    @RabbitListener(queues = "${gbpmruntime.observation.messaging.engine-events.queue-name}")
    public void handle(EngineTaskCompletedEvent event) {
        log.info(
                "Received engine task completed event: eventId={}, processDefinitionKey={}, bpmnElementId={}",
                event.getEventId(),
                event.getExecution().getProcessDefinitionKey(),
                event.getExecution().getBpmnElementId()
        );
        createKeiObservationOperation.process(CreateKeiObservationRequest
                .builder()
                .sourceEventId(event.getEventId())
                .execution(event.getExecution())
                .taskStatus(event.getTaskStatus())
                .resourceUsages(event.getResourceUsages())
                .build());
    }
}
