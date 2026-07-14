package cs.rug.monitoringresultsservice.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private BigDecimal targetValue;

    @NotNull
    private BigDecimal difference;

    @NotBlank
    private String status;
}
