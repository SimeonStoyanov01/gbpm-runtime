package cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords;

import cs.rug.monitoringresultsservice.api.base.ProcessorResponse;
import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindMonitoringRecordsResponse implements ProcessorResponse {
    private List<MonitoringRecord> records;
}
