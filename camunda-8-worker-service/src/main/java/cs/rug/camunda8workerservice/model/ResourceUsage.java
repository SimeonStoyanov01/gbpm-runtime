package cs.rug.camunda8workerservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceUsage {
    private String resourceName;
    private Double timeUsed;
    private String unit;
}
