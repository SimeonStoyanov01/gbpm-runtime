package cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords;

import cs.rug.monitoringresultsservice.api.base.Processor;
import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;

import java.util.List;

public interface FindMonitoringRecordsOperation
        extends Processor<FindMonitoringRecordsRequest, List<MonitoringRecord>> {
}
