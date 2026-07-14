package cs.rug.co2calculationservice.application.processors;

import cs.rug.co2calculationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.co2calculationservice.api.events.calculationfailed.KeiCalculationFailedEvent;
import cs.rug.co2calculationservice.api.events.calculationrequested.KeiCalculationRequestedEvent;
import cs.rug.co2calculationservice.api.model.CalculationMetadata;
import cs.rug.co2calculationservice.api.model.ResourceBreakdown;
import cs.rug.co2calculationservice.api.model.ResourceUsage;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Operation;
import cs.rug.co2calculationservice.application.CalculationFailureException;
import cs.rug.co2calculationservice.application.factory.CalculationCompletedEventFactory;
import cs.rug.co2calculationservice.application.factory.CalculationFailedEventFactory;
import cs.rug.co2calculationservice.application.model.CalculationOutcome;
import cs.rug.co2calculationservice.application.model.EmissionFactor;
import cs.rug.co2calculationservice.application.model.ResourceProfile;
import cs.rug.co2calculationservice.application.out.CalculationMetadataProvider;
import cs.rug.co2calculationservice.application.out.CalculationResultPublisher;
import cs.rug.co2calculationservice.application.out.EmissionFactorLookup;
import cs.rug.co2calculationservice.application.out.ResourceProfileLookup;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalculateCo2Processor implements CalculateCo2Operation {

    private static final String CARBON_EMISSIONS_KEI_ID = "carbon-emissions";
    private static final String RESULT_UNIT = "kg";
    private static final int RESULT_SCALE = 4;

    private final ResourceProfileLookup resourceProfileLookup;
    private final EmissionFactorLookup emissionFactorLookup;
    private final CalculationMetadataProvider calculationMetadataProvider;
    private final CalculationResultPublisher calculationResultPublisher;
    private final Validator validator;
    private final CalculationCompletedEventFactory calculationCompletedEventFactory;
    private final CalculationFailedEventFactory calculationFailedEventFactory;

    @Override
    public void process(KeiCalculationRequestedEvent request) {
        Objects.requireNonNull(request, "calculation request must not be null");
        CalculationMetadata calculation = calculationMetadataProvider.calculationMetadata();

        try {
            validate(request);
            log.info(
                    "Calculating KEI: keiId={}, processInstanceKey={}, bpmnElementId={}",
                    request.getKei().getId(),
                    request.getExecution().getProcessInstanceKey(),
                    request.getExecution().getBpmnElementId()
            );
            CalculationOutcome outcome = calculate(request, calculation);
            KeiCalculationCompletedEvent event = calculationCompletedEventFactory.create(request, outcome, calculation);
            calculationResultPublisher.publishCompleted(event);

        } catch (CalculationFailureException exception) {
            log.warn(
                    "KEI calculation failed: code={}, message={}",
                    exception.getCode(),
                    exception.getMessage()
            );
            KeiCalculationFailedEvent event = calculationFailedEventFactory.create(request, exception, calculation);
            calculationResultPublisher.publishFailed(event);
        }
    }

    private void validate(KeiCalculationRequestedEvent request) {
        Set<ConstraintViolation<KeiCalculationRequestedEvent>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String message = violations
                    .stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining("; "));
            throw CalculationFailureException.invalidRequest(message);
        }
    }

    private CalculationOutcome calculate(KeiCalculationRequestedEvent request, CalculationMetadata calculation) {
        requireSupportedKei(request);

        BigDecimal total = BigDecimal.ZERO;
        List<ResourceBreakdown> breakdown = new ArrayList<>();

        for (ResourceUsage resourceUsage : request.getInputs().getResourceUsages()) {
            ResourceBreakdown resourceBreakdown = calculateResource(resourceUsage, calculation.getReferenceSetId());
            total = total.add(resourceBreakdown.getEmissionValue());
            breakdown.add(resourceBreakdown);
        }

        return CalculationOutcome.succeeded(
                total.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                breakdown
        );
    }

    private ResourceBreakdown calculateResource(ResourceUsage resourceUsage, String referenceSetId) {
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

    private void requireSupportedKei(KeiCalculationRequestedEvent request) {
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
