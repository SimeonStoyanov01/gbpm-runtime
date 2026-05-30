package cs.rug.gbpmruntime.processregistry.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDefinitionRegistrationModel {
    private String deploymentKey;
    private String processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private String resourceName;
    private String tenantId;
    private String status;
}
