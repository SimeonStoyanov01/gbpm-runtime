package cs.rug.camunda8integration.api.operations.deployprocess;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployProcessToEngineRequest {
    @NotBlank
    private String resourceName;

    @NotEmpty
    private byte[] resourceContent;
}
