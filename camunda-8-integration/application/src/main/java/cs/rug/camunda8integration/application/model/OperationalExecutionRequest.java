package cs.rug.camunda8integration.application.model;

import cs.rug.camunda8integration.api.model.WorkObjectModel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OperationalExecutionRequest {
    private String orderId;
    private String bpmnElementId;
    private WorkObjectModel workObject;
}
