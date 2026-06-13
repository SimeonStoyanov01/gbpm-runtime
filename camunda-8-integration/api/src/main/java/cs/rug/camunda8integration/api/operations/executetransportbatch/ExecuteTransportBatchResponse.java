package cs.rug.camunda8integration.api.operations.executetransportbatch;

import cs.rug.camunda8integration.api.base.ProcessorResponse;
import cs.rug.camunda8integration.api.events.enginetaskcompleted.EngineTaskCompletedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteTransportBatchResponse implements ProcessorResponse {
    private String eventId;
    private String taskStatus;
    private Map<String, Object> completionVariables;
    private EngineTaskCompletedEvent engineTaskCompletedEvent;
}
