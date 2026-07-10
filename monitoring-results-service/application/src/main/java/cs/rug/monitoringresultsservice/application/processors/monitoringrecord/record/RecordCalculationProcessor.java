package cs.rug.monitoringresultsservice.application.processors.monitoringrecord.record;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.operations.recordcalculation.RecordCalculationOperation;
import cs.rug.monitoringresultsservice.api.operations.recordcalculation.RecordCalculationRequest;
import cs.rug.monitoringresultsservice.api.operations.recordcalculation.RecordCalculationResponse;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import cs.rug.monitoringresultsservice.application.out.MonitoringUpdatePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordCalculationProcessor implements RecordCalculationOperation {

    private final MonitoringRecordStore monitoringRecordStore;
    private final MonitoringUpdatePublisher monitoringUpdatePublisher;

    @Override
    public RecordCalculationResponse process(RecordCalculationRequest request) {
        MonitoringRecord record = monitoringRecordStore.saveCalculation(request);
        monitoringUpdatePublisher.publishCalculationUpdate(record);

        return RecordCalculationResponse
                .builder()
                .record(record)
                .build();
    }
}
