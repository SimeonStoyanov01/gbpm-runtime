package cs.rug.keievaluationservice.api.model;

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
public class ResourceBreakdown {

    @NotBlank
    private String resourceName;

    @NotNull
    private BigDecimal usageValue;

    @NotBlank
    private String usageUnit;

    @NotNull
    private BigDecimal emissionValue;

    @NotBlank
    private String unit;
}
