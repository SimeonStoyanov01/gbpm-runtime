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
public class ProcessModelElement {
    private String bpmnElementId;
    private String name;
    private String type;
    private List<ProcessModelKeiAnnotation> keiAnnotations;
}
