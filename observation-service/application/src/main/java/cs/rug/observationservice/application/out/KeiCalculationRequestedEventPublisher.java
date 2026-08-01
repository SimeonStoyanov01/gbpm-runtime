package cs.rug.observationservice.application.out;

import cs.rug.observationservice.api.events.keicalculationrequested.KeiCalculationRequestedEvent;

public interface KeiCalculationRequestedEventPublisher {

    void publish(KeiCalculationRequestedEvent event);
}
