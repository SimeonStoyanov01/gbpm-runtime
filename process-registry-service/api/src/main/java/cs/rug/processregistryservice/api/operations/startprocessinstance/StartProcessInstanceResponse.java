package cs.rug.processregistryservice.api.operations.startprocessinstance;

import cs.rug.processregistryservice.api.base.ProcessorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessInstanceResponse implements ProcessorResponse {
    private String processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private String processInstanceKey;
    private String status;
}
