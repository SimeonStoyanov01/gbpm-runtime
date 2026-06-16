package cs.rug.keievaluationservice.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceBreakdown {
    private String resourceName;
    private BigDecimal emissionValue;
    private String unit;
}
