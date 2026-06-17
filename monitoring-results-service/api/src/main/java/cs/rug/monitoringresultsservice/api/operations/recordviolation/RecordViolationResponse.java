package cs.rug.monitoringresultsservice.api.operations.recordviolation;

import cs.rug.monitoringresultsservice.api.base.ProcessorResponse;
import cs.rug.monitoringresultsservice.api.model.ThresholdViolation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordViolationResponse implements ProcessorResponse {
    private ThresholdViolation violation;
}
