package cs.rug.camunda8integration.api.operations.deployprocess;

import cs.rug.camunda8integration.api.base.ProcessorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployProcessToEngineResponse implements ProcessorResponse {
    private String deploymentKey;
    private String processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private String resourceName;
    private String tenantId;
}
