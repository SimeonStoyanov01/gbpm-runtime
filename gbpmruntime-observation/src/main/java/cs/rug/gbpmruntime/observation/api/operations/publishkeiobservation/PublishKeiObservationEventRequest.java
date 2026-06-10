package cs.rug.gbpmruntime.observation.api.operations.publishkeiobservation;

import cs.rug.gbpmruntime.common.api.base.ProcessorRequest;
import cs.rug.gbpmruntime.observation.api.events.KeiObservationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishKeiObservationEventRequest implements ProcessorRequest {
    private KeiObservationEvent event;
}
