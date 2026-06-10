package cs.rug.gbpmruntime.processregistry.infrastructure.clients.dto.deployprocess;

import cs.rug.gbpmruntime.common.api.base.ProcessorRequest;
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
