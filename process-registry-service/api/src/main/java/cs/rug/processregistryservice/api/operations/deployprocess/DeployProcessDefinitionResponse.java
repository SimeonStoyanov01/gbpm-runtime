package cs.rug.processregistryservice.api.operations.deployprocess;

import cs.rug.processregistryservice.api.base.ProcessorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployProcessDefinitionResponse implements ProcessorResponse {
    private String deploymentKey;
    private String processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private String resourceName;
    private String tenantId;
    private String status;
}
