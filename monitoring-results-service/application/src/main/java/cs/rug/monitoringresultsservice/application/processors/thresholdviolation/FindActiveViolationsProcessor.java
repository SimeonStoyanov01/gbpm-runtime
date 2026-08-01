package cs.rug.monitoringresultsservice.application.processors.thresholdviolation;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsOperation;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsRequest;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindActiveViolationsProcessor implements FindActiveViolationsOperation {

    private final MonitoringRecordStore monitoringRecordStore;

    @Override
    public List<MonitoringRecord> process(FindActiveViolationsRequest request) {
        return monitoringRecordStore.findActiveViolations(request);
    }
}
