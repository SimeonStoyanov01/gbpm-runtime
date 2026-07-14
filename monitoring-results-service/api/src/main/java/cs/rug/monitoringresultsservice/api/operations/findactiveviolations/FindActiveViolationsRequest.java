package cs.rug.monitoringresultsservice.api.operations.findactiveviolations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindActiveViolationsRequest {
    private Long processDefinitionKey;
    private String bpmnProcessId;
}
