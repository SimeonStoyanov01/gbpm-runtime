package cs.rug.gbpmruntime.observation.application.processors.requestkeicalculation;

import cs.rug.gbpmruntime.observation.api.events.keicalculationrequested.KeiCalculationRequestedEvent;
import cs.rug.gbpmruntime.observation.api.model.KeiAnnotation;
import cs.rug.gbpmruntime.observation.api.model.ResourceUsageFact;
import cs.rug.gbpmruntime.observation.api.operations.requestkeicalculation.RequestKeiCalculationOperation;
import cs.rug.gbpmruntime.observation.api.operations.requestkeicalculation.RequestKeiCalculationRequest;
import cs.rug.gbpmruntime.observation.application.factory.KeiCalculationRequestedEventFactory;
import cs.rug.gbpmruntime.observation.application.out.KeiCalculationRequestedEventPublisher;
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
