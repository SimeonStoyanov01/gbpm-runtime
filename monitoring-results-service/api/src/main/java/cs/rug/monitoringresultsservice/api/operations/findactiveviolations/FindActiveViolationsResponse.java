package cs.rug.monitoringresultsservice.api.operations.findactiveviolations;

import cs.rug.monitoringresultsservice.api.base.ProcessorResponse;
import cs.rug.monitoringresultsservice.api.model.ThresholdViolation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindActiveViolationsResponse implements ProcessorResponse {
    private List<ThresholdViolation> violations;
}
