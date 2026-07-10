package cs.rug.observationservice.api.events.keicalculationrequested;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationRequestDescriptor {
    private String strategy;
    private String referenceSetId;
}
