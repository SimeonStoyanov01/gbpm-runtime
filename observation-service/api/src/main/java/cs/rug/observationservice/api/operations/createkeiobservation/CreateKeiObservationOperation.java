package cs.rug.observationservice.api.operations.createkeiobservation;

import cs.rug.observationservice.api.base.VoidProcessor;
import cs.rug.observationservice.api.events.enginetaskcompleted.EngineTaskCompletedEvent;

public interface CreateKeiObservationOperation extends VoidProcessor<EngineTaskCompletedEvent> {
}
