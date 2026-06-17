package cs.rug.monitoringresultsservice.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineExecutionContext {
    private Long processDefinitionKey;
    private String bpmnProcessId;
    private Long processInstanceKey;
    private Long elementInstanceKey;
    private String bpmnElementId;
}
