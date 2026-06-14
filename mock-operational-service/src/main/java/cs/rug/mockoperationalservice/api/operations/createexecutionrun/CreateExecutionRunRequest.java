package cs.rug.mockoperationalservice.api.operations.createexecutionrun;

import cs.rug.mockoperationalservice.api.model.AssignedResourceModel;
import cs.rug.mockoperationalservice.api.model.WorkObjectModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private WorkObjectModel workObject;

    @Valid
    @NotEmpty
    private List<AssignedResourceModel> assignedResources;
}
