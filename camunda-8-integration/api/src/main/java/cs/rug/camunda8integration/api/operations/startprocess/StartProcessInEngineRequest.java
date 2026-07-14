package cs.rug.camunda8integration.api.operations.startprocess;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessInEngineRequest {
    @NotBlank
    @Pattern(regexp = "\\d+", message = "must be a numeric process definition key")
    private String processDefinitionKey;

    private Map<String, Object> variables;
}
