package cs.rug.processregistryservice.api.operations.deployprocess;

import cs.rug.processregistryservice.api.base.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployProcessDefinitionRequest implements ProcessorRequest {
    private String resourceName;
    private byte[] bpmnXml;
    private String targetEngine;
}
