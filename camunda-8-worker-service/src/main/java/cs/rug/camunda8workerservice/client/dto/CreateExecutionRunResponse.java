package cs.rug.camunda8workerservice.client.dto;

import cs.rug.camunda8workerservice.model.ResourceUsage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateExecutionRunResponse {
    private String runId;
    private String status;
    private List<ResourceUsage> resourceUsages;
}
