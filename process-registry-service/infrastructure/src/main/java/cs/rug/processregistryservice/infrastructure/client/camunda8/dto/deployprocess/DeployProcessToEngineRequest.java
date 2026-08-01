package cs.rug.processregistryservice.infrastructure.client.camunda8.dto.deployprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployProcessToEngineRequest {
    private String resourceName;
    private byte[] resourceContent;
}
