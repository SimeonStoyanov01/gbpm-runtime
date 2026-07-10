package cs.rug.processregistryservice.application.model.bpmn4es;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeiMetadata {
    private String id;
    private String unit;
    private String targetValue;
    private String icon;
}
