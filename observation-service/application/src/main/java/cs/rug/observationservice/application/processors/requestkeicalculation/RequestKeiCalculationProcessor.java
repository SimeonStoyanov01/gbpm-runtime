package cs.rug.observationservice.application.processors.requestkeicalculation;

import cs.rug.observationservice.api.events.keicalculationrequested.KeiCalculationRequestedEvent;
import cs.rug.observationservice.api.model.KeiAnnotation;
import cs.rug.observationservice.api.model.ResourceUsageFact;
import cs.rug.observationservice.api.operations.requestkeicalculation.RequestKeiCalculationOperation;
import cs.rug.observationservice.api.operations.requestkeicalculation.RequestKeiCalculationRequest;
import cs.rug.observationservice.application.factory.KeiCalculationRequestedEventFactory;
import cs.rug.observationservice.application.out.KeiCalculationRequestedEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestKeiCalculationProcessor implements RequestKeiCalculationOperation {

    private static final String CARBON_EMISSIONS_KEI_ID = "carbon-emissions";

    private final KeiCalculationRequestedEventPublisher keiCalculationRequestedEventPublisher;
    private final KeiCalculationRequestedEventFactory keiCalculationRequestedEventFactory;

    @Override
    public void process(RequestKeiCalculationRequest request) {
        List<KeiAnnotation> carbonEmissionAnnotations = request.getKeiAnnotations()
                .stream()
                .filter(this::isCarbonEmissionsKei)
                .toList();

        if (carbonEmissionAnnotations.isEmpty()) {
            return;
        }

        List<ResourceUsageFact> resourceUsages = request.getResourceUsages();
        if (resourceUsages == null || resourceUsages.isEmpty()) {
            log.info(
                    "Skipping KEI calculation request because resource usage data is missing: bpmnElementId={}",
                    request.getExecution().getBpmnElementId()
            );
            return;
        }

        for (KeiAnnotation keiAnnotation : carbonEmissionAnnotations) {
            KeiCalculationRequestedEvent calculationRequestedEvent = keiCalculationRequestedEventFactory.create(
                    request,
                    keiAnnotation,
                    resourceUsages
            );

            keiCalculationRequestedEventPublisher.publish(calculationRequestedEvent);

            log.info(
                    "Published KEI calculation request event: eventId={}, keiId={}, resourceUsageCount={}",
                    calculationRequestedEvent.getEventId(),
                    keiAnnotation.getId(),
                    resourceUsages.size()
            );
        }
    }

    private boolean isCarbonEmissionsKei(KeiAnnotation keiAnnotation) {
        return keiAnnotation != null && Objects.equals(CARBON_EMISSIONS_KEI_ID, keiAnnotation.getId());
    }
}
