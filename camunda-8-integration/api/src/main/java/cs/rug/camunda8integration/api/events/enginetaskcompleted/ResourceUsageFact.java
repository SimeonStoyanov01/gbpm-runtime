package cs.rug.camunda8integration.api.events.enginetaskcompleted;

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
    private Double timeUsed;
    private String unit;
}
