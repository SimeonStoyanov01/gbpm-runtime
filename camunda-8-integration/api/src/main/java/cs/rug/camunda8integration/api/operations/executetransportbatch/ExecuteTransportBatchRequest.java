package cs.rug.camunda8integration.api.operations.executetransportbatch;

import cs.rug.camunda8integration.api.base.ProcessorRequest;
import cs.rug.camunda8integration.api.model.EngineExecutionContext;
import cs.rug.camunda8integration.api.model.WorkObjectModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteTransportBatchRequest implements ProcessorRequest {
    private EngineExecutionContext engineExecutionContext;
    private String orderId;
    private WorkObjectModel workObject;
}
