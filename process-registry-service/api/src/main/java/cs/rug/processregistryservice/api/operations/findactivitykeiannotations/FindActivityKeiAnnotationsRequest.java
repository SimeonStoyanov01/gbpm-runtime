package cs.rug.processregistryservice.api.operations.findactivitykeiannotations;

import cs.rug.processregistryservice.api.base.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindActivityKeiAnnotationsRequest implements ProcessorRequest {
    private Long processDefinitionKey;
    private String bpmnElementId;
}
