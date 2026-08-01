package cs.rug.camunda8integration.api.operations.findactiveusertasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindActiveUserTasksRequest {
    @NotBlank
    @Pattern(regexp = "\\d+", message = "must be a numeric process instance key")
    private String processInstanceKey;
}
