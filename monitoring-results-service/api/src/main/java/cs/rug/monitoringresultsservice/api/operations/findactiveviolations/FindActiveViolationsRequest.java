package cs.rug.monitoringresultsservice.api.operations.findactiveviolations;

import cs.rug.monitoringresultsservice.api.base.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindActiveViolationsRequest implements ProcessorRequest {
    private Long processDefinitionKey;
    private String bpmnProcessId;
}
