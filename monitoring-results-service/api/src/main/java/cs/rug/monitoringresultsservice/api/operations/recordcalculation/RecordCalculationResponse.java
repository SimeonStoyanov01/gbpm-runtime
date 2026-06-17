package cs.rug.monitoringresultsservice.api.operations.recordcalculation;

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
public class RecordCalculationResponse implements ProcessorResponse {
    private MonitoringRecord record;
}
