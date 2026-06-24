package cs.rug.gbpmruntime.processregistry.api.operations.findactivitykeiannotations;

import cs.rug.gbpmruntime.processregistry.api.base.ProcessorRequest;
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
