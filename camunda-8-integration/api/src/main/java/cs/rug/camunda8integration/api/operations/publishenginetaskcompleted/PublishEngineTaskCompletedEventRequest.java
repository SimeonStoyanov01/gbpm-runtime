package cs.rug.camunda8integration.api.operations.publishenginetaskcompleted;

import cs.rug.camunda8integration.api.base.ProcessorRequest;
import cs.rug.camunda8integration.api.events.EngineTaskCompletedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishEngineTaskCompletedEventRequest implements ProcessorRequest {
    private EngineTaskCompletedEvent event;
}
