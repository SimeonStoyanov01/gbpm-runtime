package cs.rug.camunda8integration.application.processors.publishenginetaskcompleted;

import cs.rug.camunda8integration.api.operations.publishenginetaskcompleted.PublishEngineTaskCompletedEventOperation;
import cs.rug.camunda8integration.api.operations.publishenginetaskcompleted.PublishEngineTaskCompletedEventRequest;
import cs.rug.camunda8integration.api.operations.publishenginetaskcompleted.PublishEngineTaskCompletedEventResponse;
import cs.rug.camunda8integration.application.out.EngineTaskCompletedEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublishEngineTaskCompletedEventProcessor implements PublishEngineTaskCompletedEventOperation {

    private final EngineTaskCompletedEventPublisher engineTaskCompletedEventPublisher;

    @Override
    public PublishEngineTaskCompletedEventResponse process(PublishEngineTaskCompletedEventRequest request) {
        engineTaskCompletedEventPublisher.publish(request.getEvent());

        return PublishEngineTaskCompletedEventResponse
                .builder()
                .eventId(request.getEvent().getEventId())
                .published(true)
                .build();
    }
}
