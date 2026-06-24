package cs.rug.gbpmruntime.processregistry.application.model.bpmn4es;

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
    private List<KeiMetadata> keiMetadata;
}
