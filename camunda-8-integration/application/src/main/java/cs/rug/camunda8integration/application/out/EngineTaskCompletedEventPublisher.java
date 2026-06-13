package cs.rug.camunda8integration.application.out;

import cs.rug.camunda8integration.api.events.enginetaskcompleted.EngineTaskCompletedEvent;

public interface EngineTaskCompletedEventPublisher {

    void publish(EngineTaskCompletedEvent event);
}
