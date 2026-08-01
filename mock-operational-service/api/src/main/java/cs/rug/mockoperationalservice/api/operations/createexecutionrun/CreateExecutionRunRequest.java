package cs.rug.mockoperationalservice.api.operations.createexecutionrun;

import cs.rug.mockoperationalservice.api.model.WorkObject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExecutionRunRequest {
    @NotBlank
    private String orderId;

    @NotBlank
    private String bpmnElementId;

    @Valid
    @NotNull
    private WorkObject workObject;
}
