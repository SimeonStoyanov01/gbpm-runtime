package cs.rug.gbpmruntime.observation.application.out;

import cs.rug.gbpmruntime.observation.api.events.keiobservationcreated.KeiObservationEvent;

public interface KeiObservationEventPublisher {

    void publish(KeiObservationEvent event);
}
