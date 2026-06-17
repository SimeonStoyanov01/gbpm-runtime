package cs.rug.monitoringresultsservice.application.recordevaluation;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.operations.recordevaluation.RecordEvaluationOperation;
import cs.rug.monitoringresultsservice.api.operations.recordevaluation.RecordEvaluationRequest;
import cs.rug.monitoringresultsservice.api.operations.recordevaluation.RecordEvaluationResponse;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import cs.rug.monitoringresultsservice.application.out.MonitoringUpdatePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordEvaluationProcessor implements RecordEvaluationOperation {

    private final MonitoringRecordStore monitoringRecordStore;
    private final MonitoringUpdatePublisher monitoringUpdatePublisher;

    @Override
    public RecordEvaluationResponse process(RecordEvaluationRequest request) {
        MonitoringRecord record = monitoringRecordStore.saveEvaluation(request);
        monitoringUpdatePublisher.publishEvaluationUpdate(record);

        return RecordEvaluationResponse
                .builder()
                .record(record)
                .build();
    }
}
