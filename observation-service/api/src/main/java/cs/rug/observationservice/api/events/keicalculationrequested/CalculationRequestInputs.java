package cs.rug.observationservice.api.events.keicalculationrequested;

import cs.rug.observationservice.api.model.ResourceUsageFact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationRequestInputs {
    private List<ResourceUsageFact> resourceUsages;
}
