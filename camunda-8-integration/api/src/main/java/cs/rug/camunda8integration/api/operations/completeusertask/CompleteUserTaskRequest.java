package cs.rug.camunda8integration.api.operations.completeusertask;

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
public class CompleteUserTaskRequest {
    @NotBlank
    @Pattern(regexp = "\\d+", message = "must be a numeric user task key")
    private String userTaskKey;

    @Builder.Default
    private Map<String, Object> variables = Map.of();
}
