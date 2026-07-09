package cs.rug.camunda8workerservice.client.dto;

import cs.rug.camunda8workerservice.model.WorkObjectModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExecutionRunRequestDto {
    private String orderId;
    private String bpmnElementId;
    private WorkObjectModel workObject;
}
