package cs.rug.gbpmruntime.observation.infrastructure.client.processregistry.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FindActivityKeiAnnotationsResponseDto {
    private Long processDefinitionKey;
    private String bpmnElementId;
    private List<ProcessRegistryKeiAnnotationDto> bpmn4esKeiAnnotations;
}
