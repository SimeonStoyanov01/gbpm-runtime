package cs.rug.keievaluationservice.api.events.evaluationcompleted;

import cs.rug.keievaluationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.keievaluationservice.api.model.EvaluationDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeiEvaluationCompletedEvent {
    private String eventId;
    private Instant occurredAt;
    private KeiCalculationCompletedEvent calculationEvent;
    private EvaluationDetails evaluation;
}
