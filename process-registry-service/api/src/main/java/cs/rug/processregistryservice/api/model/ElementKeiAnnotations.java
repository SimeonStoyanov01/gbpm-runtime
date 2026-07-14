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
public class ElementKeiAnnotations {
    private String bpmnElementId;
    private String elementName;
    private String elementType;
    private List<KeiAnnotation> keiAnnotations;
}
