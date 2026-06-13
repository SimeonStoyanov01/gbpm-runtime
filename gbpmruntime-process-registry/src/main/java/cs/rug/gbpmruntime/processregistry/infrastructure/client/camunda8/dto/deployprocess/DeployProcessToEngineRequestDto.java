package cs.rug.gbpmruntime.processregistry.infrastructure.client.camunda8.dto.deployprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployProcessToEngineRequestDto {
    private String resourceName;
    private byte[] resourceContent;
}
