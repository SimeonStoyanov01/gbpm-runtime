package cs.rug.monitoringresultsservice.api.events.evaluationcompleted;

import cs.rug.monitoringresultsservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.monitoringresultsservice.api.model.EvaluationDetails;
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
