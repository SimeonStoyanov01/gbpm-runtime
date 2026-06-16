package cs.rug.keievaluationservice.application.out;

import cs.rug.keievaluationservice.api.events.evaluationcompleted.KeiEvaluationCompletedEvent;

public interface EvaluationResultPublisher {

    void publish(KeiEvaluationCompletedEvent event);
}
