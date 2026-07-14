package cs.rug.monitoringresultsservice.application.processors.monitoringrecord.record;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.operations.recordevaluation.RecordEvaluationOperation;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import cs.rug.monitoringresultsservice.application.out.MonitoringUpdatePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordEvaluationProcessor implements RecordEvaluationOperation {

    private static final String EVALUATION_STATUS_VIOLATED = "VIOLATED";

    private final MonitoringRecordStore monitoringRecordStore;
    private final MonitoringUpdatePublisher monitoringUpdatePublisher;

    @Override
    public void process(MonitoringRecord record) {
        MonitoringRecord savedRecord = monitoringRecordStore.saveEvaluation(record);

        monitoringUpdatePublisher.publishEvaluationUpdate(savedRecord);
        if (EVALUATION_STATUS_VIOLATED.equals(savedRecord.getEvaluationStatus())) {
            monitoringUpdatePublisher.publishViolationUpdate(savedRecord);
        }
    }
}
