package cs.rug.camunda8integration.infrastructure.client.mockoperational.dto;

import cs.rug.camunda8integration.api.model.WorkObjectModel;
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
