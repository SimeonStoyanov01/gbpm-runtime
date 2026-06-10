package cs.rug.gbpmruntime.observation.api.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmnContext {
    private String processId;
    private String elementId;
}
