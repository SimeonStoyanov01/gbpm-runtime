package cs.rug.processregistryservice.infrastructure.client.camunda8.dto.startprocess;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessInEngineResponse {
    private String processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private String processInstanceKey;
    private String tenantId;
}
