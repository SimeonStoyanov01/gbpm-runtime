package cs.rug.gbpmruntime.processregistry.infrastructure.client.monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessModelKeiAnnotationDto {
    private String id;
    private String unit;
    private String targetValue;
    private String icon;
}
