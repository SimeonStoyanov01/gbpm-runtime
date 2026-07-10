package cs.rug.observationservice.application.out;

import cs.rug.observationservice.api.events.keiobservationcreated.KeiObservationEvent;

public interface KeiObservationEventPublisher {

    void publish(KeiObservationEvent event);
}
