package cs.rug.monitoringresultsservice.application.processors.monitoringrecord.find;

import cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails.FindProcessInstanceDetailsOperation;
import cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails.FindProcessInstanceDetailsRequest;
import cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails.FindProcessInstanceDetailsResponse;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindProcessInstanceDetailsProcessor implements FindProcessInstanceDetailsOperation {

    private final MonitoringRecordStore monitoringRecordStore;

    @Override
    public FindProcessInstanceDetailsResponse process(FindProcessInstanceDetailsRequest request) {
        return monitoringRecordStore.findProcessInstanceDetails(request);
    }
}
