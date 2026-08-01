package cs.rug.co2calculationservice.application.model;

import cs.rug.co2calculationservice.api.model.ResourceBreakdown;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CalculationOutcome {

    private final BigDecimal value;
    private final String unit;
    private final List<ResourceBreakdown> resourceBreakdown;

    private CalculationOutcome(
            BigDecimal value,
            String unit,
            List<ResourceBreakdown> resourceBreakdown
    ) {
        this.value = value;
        this.unit = unit;
        this.resourceBreakdown = resourceBreakdown;
    }

    public static CalculationOutcome succeeded(
            BigDecimal value,
            String unit,
            List<ResourceBreakdown> resourceBreakdown
    ) {
        return new CalculationOutcome(
                value,
                unit,
                resourceBreakdown
        );
    }
}
