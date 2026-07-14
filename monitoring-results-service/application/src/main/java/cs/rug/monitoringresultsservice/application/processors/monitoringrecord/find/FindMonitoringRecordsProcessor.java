package cs.rug.monitoringresultsservice.application.processors.monitoringrecord.find;

import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsOperation;
import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsRequest;
import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindMonitoringRecordsProcessor implements FindMonitoringRecordsOperation {

    private final MonitoringRecordStore monitoringRecordStore;

    @Override
    public List<MonitoringRecord> process(FindMonitoringRecordsRequest request) {
        return monitoringRecordStore.findMonitoringRecords(request);
    }
}
