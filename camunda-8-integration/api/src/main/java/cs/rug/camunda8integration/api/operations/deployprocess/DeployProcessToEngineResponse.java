package cs.rug.camunda8integration.api.operations.deployprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployProcessToEngineResponse {
    private String deploymentKey;
    private String processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private String resourceName;
    private String tenantId;
}
