package cs.rug.keievaluationservice.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculatedResult {
    private BigDecimal value;
    private String unit;
    private Instant calculatedAt;
}
