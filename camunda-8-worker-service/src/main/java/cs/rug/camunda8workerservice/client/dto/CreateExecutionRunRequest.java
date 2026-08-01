package cs.rug.camunda8workerservice.client.dto;

import cs.rug.camunda8workerservice.model.WorkObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExecutionRunRequest {
    private String orderId;
    private String bpmnElementId;
    private WorkObject workObject;
}
