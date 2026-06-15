package cs.rug.co2calculationservice.application.model;

import cs.rug.co2calculationservice.api.model.CalculationError;
import cs.rug.co2calculationservice.api.model.ResourceBreakdown;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CalculationOutcome {

    private static final String SUCCEEDED_STATUS = "SUCCEEDED";
    private static final String FAILED_STATUS = "FAILED";
    private static final String RESULT_UNIT = "kgCO2e";

    private final String status;
    private final BigDecimal value;
    private final String unit;
    private final List<ResourceBreakdown> resourceBreakdown;
    private final List<CalculationError> errors;

    private CalculationOutcome(
            String status,
            BigDecimal value,
            String unit,
            List<ResourceBreakdown> resourceBreakdown,
            List<CalculationError> errors
    ) {
        this.status = status;
        this.value = value;
        this.unit = unit;
        this.resourceBreakdown = resourceBreakdown;
        this.errors = errors;
    }

    public static CalculationOutcome succeeded(
            BigDecimal value,
            List<ResourceBreakdown> resourceBreakdown
    ) {
        return new CalculationOutcome(
                SUCCEEDED_STATUS,
                value,
                RESULT_UNIT,
                resourceBreakdown,
                List.of()
        );
    }

    public static CalculationOutcome failed(String code, String message) {
        return new CalculationOutcome(
                FAILED_STATUS,
                null,
                RESULT_UNIT,
                List.of(),
                List.of(CalculationError
                        .builder()
                        .code(code)
                        .message(message)
                        .build())
        );
    }
}
