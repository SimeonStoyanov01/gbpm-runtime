package cs.rug.monitoringresultsservice.api.operations.registerprocessmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterProcessModelRequest {
    private Long deploymentKey;
    private Long processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private String resourceName;
    private String bpmnXml;
    private List<ProcessModelElement> elements;
}
