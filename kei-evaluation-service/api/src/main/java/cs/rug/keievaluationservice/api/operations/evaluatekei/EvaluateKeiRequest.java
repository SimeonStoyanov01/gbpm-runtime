package cs.rug.keievaluationservice.api.operations.evaluatekei;

import cs.rug.keievaluationservice.api.base.ProcessorRequest;
import cs.rug.keievaluationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateKeiRequest implements ProcessorRequest {
    @Valid
    @NotNull
    private KeiCalculationCompletedEvent event;
}
