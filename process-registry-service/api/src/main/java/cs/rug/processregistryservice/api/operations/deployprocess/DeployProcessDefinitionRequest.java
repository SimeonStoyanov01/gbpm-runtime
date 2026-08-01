package cs.rug.processregistryservice.api.operations.deployprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployProcessDefinitionRequest {
    private String resourceName;
    private byte[] bpmnXml;
    private String targetEngine;
}
