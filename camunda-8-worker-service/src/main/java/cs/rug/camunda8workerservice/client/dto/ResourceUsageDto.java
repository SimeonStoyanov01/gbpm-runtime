package cs.rug.camunda8workerservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceUsageDto {
    private String resourceName;
    private String timeUsed;
    private String unit;
}
