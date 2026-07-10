package cs.rug.observationservice.application.factory;

import cs.rug.observationservice.api.events.keicalculationrequested.CalculationRequestDescriptor;
import cs.rug.observationservice.api.events.keicalculationrequested.CalculationRequestInputs;
import cs.rug.observationservice.api.events.keicalculationrequested.KeiCalculationRequestedEvent;
import cs.rug.observationservice.api.model.KeiAnnotation;
import cs.rug.observationservice.api.model.ResourceUsageFact;
import cs.rug.observationservice.api.operations.requestkeicalculation.RequestKeiCalculationRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class KeiCalculationRequestedEventFactory {

    private static final String EVENT_TYPE = "KEI_CALCULATION_REQUESTED";
    private static final String POPESCU_RESOURCE_CO2_STRATEGY = "POPESCU_RESOURCE_CO2";
    private static final String POPESCU_DEMO_REFERENCE_SET_ID = "popescu-demo-v1";

    public KeiCalculationRequestedEvent create(
            RequestKeiCalculationRequest request,
            KeiAnnotation keiAnnotation,
            List<ResourceUsageFact> resourceUsages
    ) {
        return KeiCalculationRequestedEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EVENT_TYPE)
                .occurredAt(Instant.now())
                .calculation(CalculationRequestDescriptor
                        .builder()
                        .strategy(POPESCU_RESOURCE_CO2_STRATEGY)
                        .referenceSetId(POPESCU_DEMO_REFERENCE_SET_ID)
                        .build())
                .kei(keiAnnotation)
                .execution(request.getExecution())
                .inputs(CalculationRequestInputs
                        .builder()
                        .resourceUsages(resourceUsages)
                        .build())
                .build();
    }
}
