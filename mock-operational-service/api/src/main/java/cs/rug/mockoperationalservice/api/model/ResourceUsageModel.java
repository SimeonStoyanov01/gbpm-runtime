package cs.rug.mockoperationalservice.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceUsageModel {
    private String resourceName;
    private Double timeUsed;
    private String unit;
}
