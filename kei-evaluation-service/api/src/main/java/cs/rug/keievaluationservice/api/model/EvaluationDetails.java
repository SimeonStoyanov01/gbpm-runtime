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
public class EvaluationDetails {
    private String status;
    private String operator;
    private BigDecimal targetValue;
    private BigDecimal difference;
    private String unit;
}
