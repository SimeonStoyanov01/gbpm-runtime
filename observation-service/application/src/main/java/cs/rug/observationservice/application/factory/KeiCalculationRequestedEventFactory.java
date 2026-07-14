package cs.rug.observationservice.application.factory;

import cs.rug.observationservice.api.events.keicalculationrequested.CalculationInputs;
import cs.rug.observationservice.api.events.keicalculationrequested.KeiCalculationRequestedEvent;
import cs.rug.observationservice.api.model.EngineExecutionContext;
import cs.rug.observationservice.api.model.KeiAnnotation;
import cs.rug.observationservice.api.model.ResourceUsage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeiCalculationRequestedEventFactory {

    public KeiCalculationRequestedEvent create(
            EngineExecutionContext execution,
            KeiAnnotation keiAnnotation,
            List<ResourceUsage> resourceUsages
    ) {
        return KeiCalculationRequestedEvent
                .builder()
                .kei(keiAnnotation)
                .execution(execution)
                .inputs(CalculationInputs
                        .builder()
                        .resourceUsages(resourceUsages)
                        .build())
                .build();
    }
}
