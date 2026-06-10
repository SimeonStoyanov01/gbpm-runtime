package cs.rug.gbpmruntime.observation.api.operations.publishkeiobservation;

import cs.rug.gbpmruntime.common.api.base.ProcessorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishKeiObservationEventResponse implements ProcessorResponse {
    private String eventId;
    private Boolean published;
}
