package cs.rug.processregistryservice.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElementKeiAnnotationsModel {
    private String bpmnElementId;
    private List<KeiAnnotationModel> bpmn4esKeiAnnotations;
}
