package cs.rug.monitoringresultsservice.api.operations.registerprocessmodel;

import cs.rug.monitoringresultsservice.api.base.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterProcessModelRequest implements ProcessorRequest {
    private Long deploymentKey;
    private Long processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private String resourceName;
    private List<ProcessModelElement> elements;
}
