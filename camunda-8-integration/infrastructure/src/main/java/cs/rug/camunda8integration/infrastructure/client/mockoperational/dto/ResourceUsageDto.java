package cs.rug.camunda8integration.infrastructure.client.mockoperational.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceUsageDto {
    private String resourceName;
    private Double timeUsed;
    private String unit;
}
