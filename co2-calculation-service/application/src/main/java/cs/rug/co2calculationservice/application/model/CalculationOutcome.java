package cs.rug.co2calculationservice.application.model;

import cs.rug.co2calculationservice.api.model.ResourceBreakdown;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CalculationOutcome {

    private static final String RESULT_UNIT = "kg";

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
            List<ResourceBreakdown> resourceBreakdown
    ) {
        return new CalculationOutcome(
                value,
                RESULT_UNIT,
                resourceBreakdown
        );
    }
}
