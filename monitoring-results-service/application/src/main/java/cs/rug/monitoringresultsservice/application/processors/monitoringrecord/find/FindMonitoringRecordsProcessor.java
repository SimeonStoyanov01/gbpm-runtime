package cs.rug.monitoringresultsservice.application.processors.monitoringrecord.find;

import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsOperation;
import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsRequest;
import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsResponse;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindMonitoringRecordsProcessor implements FindMonitoringRecordsOperation {

    private final MonitoringRecordStore monitoringRecordStore;

    @Override
    public FindMonitoringRecordsResponse process(FindMonitoringRecordsRequest request) {
        return FindMonitoringRecordsResponse
                .builder()
                .records(monitoringRecordStore.findMonitoringRecords(request))
                .build();
    }
}
