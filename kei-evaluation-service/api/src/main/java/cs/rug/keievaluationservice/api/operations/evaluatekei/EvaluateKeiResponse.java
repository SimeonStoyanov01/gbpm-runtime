package cs.rug.keievaluationservice.api.operations.evaluatekei;

import cs.rug.keievaluationservice.api.base.ProcessorResponse;
import cs.rug.keievaluationservice.api.events.evaluationcompleted.KeiEvaluationCompletedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateKeiResponse implements ProcessorResponse {
    private String calculationResultId;
    private Boolean evaluated;
    private KeiEvaluationCompletedEvent event;
}
