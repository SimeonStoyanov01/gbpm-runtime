package cs.rug.processregistryservice.infrastructure.client.monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterProcessModelRequestDto {
    private Long deploymentKey;
    private Long processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private String resourceName;
    private String bpmnXml;
    private List<ProcessModelElementDto> elements;
}
