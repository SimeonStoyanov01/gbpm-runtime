package cs.rug.keievaluationservice.infrastructure.messaging.inbound;

import cs.rug.keievaluationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.keievaluationservice.api.operations.evaluatekei.EvaluateKeiRequest;
import org.springframework.stereotype.Component;

@Component
public class CalculationResultEventMapper {

    public EvaluateKeiRequest toRequest(KeiCalculationCompletedEvent event) {
        return EvaluateKeiRequest
                .builder()
                .event(event)
                .build();
    }
}
