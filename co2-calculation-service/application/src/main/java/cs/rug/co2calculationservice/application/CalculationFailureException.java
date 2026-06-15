package cs.rug.co2calculationservice.application;

import cs.rug.co2calculationservice.api.model.ResourceUsage;
import cs.rug.co2calculationservice.application.model.EmissionFactor;
import cs.rug.co2calculationservice.application.model.ResourceProfile;
import lombok.Getter;

@Getter
public class CalculationFailureException extends RuntimeException {

    private final String code;

    private CalculationFailureException(String code, String message) {
        super(message);
        this.code = code;
    }

    public static CalculationFailureException invalidRequest(String message) {
        return new CalculationFailureException(
                "INVALID_CALCULATION_REQUEST",
                "Invalid calculation request: " + message
        );
    }

    public static CalculationFailureException unsupportedStrategy(String strategy) {
        return new CalculationFailureException(
                "UNSUPPORTED_CALCULATION_STRATEGY",
                "Unsupported calculation strategy: " + strategy
        );
    }

    public static CalculationFailureException unsupportedKei(String keiId) {
        return new CalculationFailureException(
                "UNSUPPORTED_KEI",
                "Unsupported KEI id: " + keiId
        );
    }

    public static CalculationFailureException resourceProfileNotFound(
            String referenceSetId,
            String resourceName
    ) {
        return new CalculationFailureException(
                "RESOURCE_PROFILE_NOT_FOUND",
                "No resource profile found for resourceName=" + resourceName
                        + " in referenceSetId=" + referenceSetId
        );
    }

    public static CalculationFailureException emissionFactorNotFound(
            String referenceSetId,
            String fuelType
    ) {
        return new CalculationFailureException(
                "EMISSION_FACTOR_NOT_FOUND",
                "No emission factor found for fuelType=" + fuelType
                        + " in referenceSetId=" + referenceSetId
        );
    }

    public static CalculationFailureException resourceUsageUnitMismatch(
            ResourceUsage resourceUsage,
            ResourceProfile resourceProfile
    ) {
        return new CalculationFailureException(
                "RESOURCE_USAGE_UNIT_MISMATCH",
                "Resource usage unit " + resourceUsage.getUnit()
                        + " does not match expected timeUnit=" + resourceProfile.getTimeUnit()
                        + " for resourceName=" + resourceUsage.getResourceName()
        );
    }

    public static CalculationFailureException fuelUnitMismatch(
            ResourceProfile resourceProfile,
            EmissionFactor emissionFactor
    ) {
        return new CalculationFailureException(
                "FUEL_UNIT_MISMATCH",
                "Resource fuel unit " + resourceProfile.getFuelUnit()
                        + " does not match emission factor unit=" + emissionFactor.getUnit()
                        + " for fuelType=" + resourceProfile.getFuelType()
        );
    }
}
