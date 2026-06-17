package cs.rug.monitoringresultsservice.api.operations.recordevaluation;

import cs.rug.monitoringresultsservice.api.base.ProcessorResponse;
import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordEvaluationResponse implements ProcessorResponse {
    private MonitoringRecord record;
}
