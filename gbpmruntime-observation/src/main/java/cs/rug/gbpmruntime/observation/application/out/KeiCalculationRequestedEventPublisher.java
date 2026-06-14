package cs.rug.gbpmruntime.observation.application.out;

import cs.rug.gbpmruntime.observation.api.events.keicalculationrequested.KeiCalculationRequestedEvent;

public interface KeiCalculationRequestedEventPublisher {

    void publish(KeiCalculationRequestedEvent event);
}
