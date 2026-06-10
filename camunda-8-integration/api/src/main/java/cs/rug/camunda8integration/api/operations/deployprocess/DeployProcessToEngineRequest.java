package cs.rug.camunda8integration.api.operations.deployprocess;

import cs.rug.camunda8integration.api.base.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployProcessToEngineRequest implements ProcessorRequest {
    private String resourceName;
    private byte[] resourceContent;
}
