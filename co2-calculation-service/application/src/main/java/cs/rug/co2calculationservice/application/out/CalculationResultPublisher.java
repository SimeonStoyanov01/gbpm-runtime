package cs.rug.co2calculationservice.application.out;

import cs.rug.co2calculationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.co2calculationservice.api.events.calculationfailed.KeiCalculationFailedEvent;

public interface CalculationResultPublisher {

    void publishCompleted(KeiCalculationCompletedEvent event);

    void publishFailed(KeiCalculationFailedEvent event);
}
