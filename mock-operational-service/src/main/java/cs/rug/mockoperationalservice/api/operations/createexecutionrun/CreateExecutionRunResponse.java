package cs.rug.mockoperationalservice.api.operations.createexecutionrun;

import cs.rug.mockoperationalservice.api.model.ResourceUsageModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExecutionRunResponse {
    private String runId;
    private String status;
    private List<ResourceUsageModel> resourceUsages;
}
