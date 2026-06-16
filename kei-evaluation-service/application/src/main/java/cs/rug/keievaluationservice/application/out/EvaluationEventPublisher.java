package cs.rug.keievaluationservice.application.out;

import cs.rug.keievaluationservice.api.events.evaluationcompleted.KeiEvaluationCompletedEvent;
import cs.rug.keievaluationservice.api.events.thresholdviolationdetected.ThresholdViolationDetectedEvent;

public interface EvaluationEventPublisher {

    void publishEvaluation(KeiEvaluationCompletedEvent event);

    void publishViolation(ThresholdViolationDetectedEvent event);

}
