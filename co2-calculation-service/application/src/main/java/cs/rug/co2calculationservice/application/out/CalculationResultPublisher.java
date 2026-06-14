package cs.rug.co2calculationservice.application.out;

import cs.rug.co2calculationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;

public interface CalculationResultPublisher {

    void publish(KeiCalculationCompletedEvent event);
}
