package cs.rug.processregistryservice.api.operations.startprocessinstance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessInstanceResponse {
    private String processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private String processInstanceKey;
    private String status;
}
