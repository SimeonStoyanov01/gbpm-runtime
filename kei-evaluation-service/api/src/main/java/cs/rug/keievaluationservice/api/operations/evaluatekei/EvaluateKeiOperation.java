package cs.rug.keievaluationservice.api.operations.evaluatekei;

import cs.rug.keievaluationservice.api.base.VoidProcessor;
import cs.rug.keievaluationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;

public interface EvaluateKeiOperation extends VoidProcessor<KeiCalculationCompletedEvent> {
}
