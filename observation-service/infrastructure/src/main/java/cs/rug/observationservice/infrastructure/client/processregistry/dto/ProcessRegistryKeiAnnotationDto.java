package cs.rug.observationservice.infrastructure.client.processregistry.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProcessRegistryKeiAnnotationDto {
    private String id;
    private String unit;
    private String targetValue;
    private String icon;
}
