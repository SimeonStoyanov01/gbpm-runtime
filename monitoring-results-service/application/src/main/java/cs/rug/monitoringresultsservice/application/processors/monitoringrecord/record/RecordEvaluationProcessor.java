package cs.rug.monitoringresultsservice.application.processors.monitoringrecord.record;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.model.ThresholdViolation;
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

    private static final String EVALUATION_STATUS_VIOLATED = "VIOLATED";

    private final MonitoringRecordStore monitoringRecordStore;
    private final MonitoringUpdatePublisher monitoringUpdatePublisher;

    @Override
    public RecordEvaluationResponse process(RecordEvaluationRequest request) {
        MonitoringRecord record = monitoringRecordStore.saveEvaluation(request);

        monitoringUpdatePublisher.publishEvaluationUpdate(record);
        if (EVALUATION_STATUS_VIOLATED.equals(record.getEvaluationStatus())) {
            monitoringUpdatePublisher.publishViolationUpdate(violationFrom(record));
        }

        return RecordEvaluationResponse
                .builder()
                .record(record)
                .build();
    }

    private ThresholdViolation violationFrom(MonitoringRecord record) {
        return ThresholdViolation
                .builder()
                .eventId(record.getEvaluationEventId())
                .processDefinitionKey(record.getProcessDefinitionKey())
                .bpmnProcessId(record.getBpmnProcessId())
                .serviceTaskId(record.getBpmnElementId())
                .processInstanceKey(record.getProcessInstanceKey())
                .emissionType(record.getKeiId())
                .calculatedValue(record.getCalculatedValue())
                .targetValue(record.getTargetValue())
                .difference(record.getDifference())
                .status(record.getEvaluationStatus())
                .occurredAt(record.getEvaluatedAt())
                .build();
    }
}
