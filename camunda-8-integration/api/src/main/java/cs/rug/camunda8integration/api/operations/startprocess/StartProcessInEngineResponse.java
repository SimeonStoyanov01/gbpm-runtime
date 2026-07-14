package cs.rug.camunda8integration.api.operations.startprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessInEngineResponse {
    private String processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private String processInstanceKey;
    private String tenantId;
}
