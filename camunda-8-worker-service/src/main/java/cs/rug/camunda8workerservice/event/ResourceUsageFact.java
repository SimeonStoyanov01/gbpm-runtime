package cs.rug.camunda8workerservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceUsageFact {
    private String resourceName;
    private String timeUsed;
    private String unit;
}
