package cs.rug.co2calculationservice.api.operations.calculateco2;

import cs.rug.co2calculationservice.api.base.ProcessorResponse;
import cs.rug.co2calculationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculateCo2Response implements ProcessorResponse {
    private String eventId;
    private String status;
    private KeiCalculationCompletedEvent event;
}
