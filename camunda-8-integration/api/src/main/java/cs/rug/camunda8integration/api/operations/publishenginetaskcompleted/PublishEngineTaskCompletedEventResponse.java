package cs.rug.camunda8integration.api.operations.publishenginetaskcompleted;

import cs.rug.camunda8integration.api.base.ProcessorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishEngineTaskCompletedEventResponse implements ProcessorResponse {
    private String eventId;
    private Boolean published;
}
