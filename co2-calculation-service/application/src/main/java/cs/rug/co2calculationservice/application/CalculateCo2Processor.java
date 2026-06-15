package cs.rug.co2calculationservice.application;

import cs.rug.co2calculationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.co2calculationservice.api.model.ResourceBreakdown;
import cs.rug.co2calculationservice.api.model.ResourceUsage;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Operation;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Request;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Response;
import cs.rug.co2calculationservice.application.factory.CalculationCompletedEventFactory;
import cs.rug.co2calculationservice.application.model.CalculationOutcome;
import cs.rug.co2calculationservice.application.model.EmissionFactor;
import cs.rug.co2calculationservice.application.model.ResourceProfile;
import cs.rug.co2calculationservice.application.out.CalculationResultPublisher;
import cs.rug.co2calculationservice.application.out.EmissionFactorLookup;
import cs.rug.co2calculationservice.application.out.ResourceProfileLookup;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalculateCo2Processor implements CalculateCo2Operation {

    private static final String POPESCU_RESOURCE_CO2_STRATEGY = "POPESCU_RESOURCE_CO2";
    private static final String CARBON_EMISSIONS_KEI_ID = "carbon-emissions";
    private static final String RESULT_UNIT = "kgCO2e";
    private static final int RESULT_SCALE = 4;

    private final ResourceProfileLookup resourceProfileLookup;
    private final EmissionFactorLookup emissionFactorLookup;
    private final CalculationResultPublisher calculationResultPublisher;
    private final Validator validator;
    private final CalculationCompletedEventFactory calculationCompletedEventFactory;

    @Override
    public CalculateCo2Response process(CalculateCo2Request request) {
        Objects.requireNonNull(request, "request must not be null");

        CalculationOutcome outcome;
        try {
            validate(request);
            outcome = calculate(request);
        } catch (CalculationFailureException exception) {
            outcome = CalculationOutcome.failed(exception.getCode(), exception.getMessage());
        }

        KeiCalculationCompletedEvent event = calculationCompletedEventFactory.create(request, outcome);
        calculationResultPublisher.publish(event);

        return CalculateCo2Response
                .builder()
                .eventId(event.getEventId())
                .status(outcome.getStatus())
                .event(event)
                .build();
    }

    private void validate(CalculateCo2Request request) {
        Set<ConstraintViolation<CalculateCo2Request>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String message = violations
                    .stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining("; "));
            throw CalculationFailureException.invalidRequest(message);
        }
    }

    private CalculationOutcome calculate(CalculateCo2Request request) {
        requireSupportedStrategy(request);
        requireSupportedKei(request);

        BigDecimal total = BigDecimal.ZERO;
        List<ResourceBreakdown> breakdown = new ArrayList<>();

        for (ResourceUsage resourceUsage : request.getInputs().getResourceUsages()) {
            ResourceBreakdown resourceBreakdown = calculateResource(request, resourceUsage);
            total = total.add(resourceBreakdown.getEmissionValue());
            breakdown.add(resourceBreakdown);
        }

        return CalculationOutcome.succeeded(
                total.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                breakdown
        );
    }

    private ResourceBreakdown calculateResource(CalculateCo2Request request, ResourceUsage resourceUsage) {
        String referenceSetId = request.getCalculation().getReferenceSetId();
        ResourceProfile resourceProfile = resourceProfileLookup
                .findResourceProfile(referenceSetId, resourceUsage.getResourceName())
                .orElseThrow(() -> CalculationFailureException.resourceProfileNotFound(
                        referenceSetId,
                        resourceUsage.getResourceName()
                ));

        requireResourceUsageUnit(resourceUsage, resourceProfile);

        EmissionFactor emissionFactor = emissionFactorLookup
                .findEmissionFactor(referenceSetId, resourceProfile.getFuelType())
                .orElseThrow(() -> CalculationFailureException.emissionFactorNotFound(
                        referenceSetId,
                        resourceProfile.getFuelType()
                ));

        requireFuelUnit(resourceProfile, emissionFactor);

        BigDecimal timeUsed = BigDecimal.valueOf(resourceUsage.getTimeUsed());
        BigDecimal consumedAmount = timeUsed.multiply(resourceProfile.getFuelPerUse());
        BigDecimal emissionValue = consumedAmount
                .multiply(emissionFactor.getFactor())
                .setScale(RESULT_SCALE, RoundingMode.HALF_UP);

        return ResourceBreakdown
                .builder()
                .resourceName(resourceUsage.getResourceName())
                .emissionValue(emissionValue)
                .unit(RESULT_UNIT)
                .build();
    }

    private void requireSupportedStrategy(CalculateCo2Request request) {
        if (!POPESCU_RESOURCE_CO2_STRATEGY.equals(request.getCalculation().getStrategy())) {
            throw CalculationFailureException.unsupportedStrategy(request.getCalculation().getStrategy());
        }
    }

    private void requireSupportedKei(CalculateCo2Request request) {
        if (!CARBON_EMISSIONS_KEI_ID.equals(request.getKei().getId())) {
            throw CalculationFailureException.unsupportedKei(request.getKei().getId());
        }
    }

    private void requireResourceUsageUnit(ResourceUsage resourceUsage, ResourceProfile resourceProfile) {
        if (!equalsIgnoreCase(resourceUsage.getUnit(), resourceProfile.getTimeUnit())) {
            throw CalculationFailureException.resourceUsageUnitMismatch(resourceUsage, resourceProfile);
        }
    }

    private void requireFuelUnit(ResourceProfile resourceProfile, EmissionFactor emissionFactor) {
        if (!equalsIgnoreCase(resourceProfile.getFuelUnit(), emissionFactor.getUnit())) {
            throw CalculationFailureException.fuelUnitMismatch(resourceProfile, emissionFactor);
        }
    }

    private boolean equalsIgnoreCase(String first, String second) {
        return first != null && second != null && first.equalsIgnoreCase(second);
    }
}
