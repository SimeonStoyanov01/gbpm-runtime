package cs.rug.monitoringresultsservice.infrastructure.persistence.placeholder;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.model.ThresholdViolation;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsRequest;
import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsRequest;
import cs.rug.monitoringresultsservice.api.operations.recordcalculation.RecordCalculationRequest;
import cs.rug.monitoringresultsservice.api.operations.recordevaluation.RecordEvaluationRequest;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import cs.rug.monitoringresultsservice.application.out.ThresholdViolationStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryMonitoringStore implements MonitoringRecordStore, ThresholdViolationStore {

    private final ConcurrentMap<String, MonitoringRecord> recordsByCalculationEventId = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, ThresholdViolation> activeViolationsByEventId = new ConcurrentHashMap<>();

    @Override
    public MonitoringRecord saveCalculation(RecordCalculationRequest request) {
        return recordsByCalculationEventId.compute(
                request.getEventId(),
                (calculationEventId, existing) -> calculationRecord(request, existing)
        );
    }

    @Override
    public MonitoringRecord saveEvaluation(RecordEvaluationRequest request) {
        return recordsByCalculationEventId.compute(
                request.getCalculationResultId(),
                (calculationEventId, existing) -> evaluationRecord(request, existing)
        );
    }

    @Override
    public void saveViolationMarker(ThresholdViolation violation) {
    }

    @Override
    public List<MonitoringRecord> findMonitoringRecords(FindMonitoringRecordsRequest request) {
        return recordsByCalculationEventId
                .values()
                .stream()
                .filter(record -> matchesMonitoringFilter(record, request))
                .toList();
    }

    @Override
    public ThresholdViolation saveOrUpdateActiveViolation(ThresholdViolation violation) {
        activeViolationsByEventId.put(violation.getEventId(), violation);
        return violation;
    }

    @Override
    public List<ThresholdViolation> findActiveViolations(FindActiveViolationsRequest request) {
        return activeViolationsByEventId
                .values()
                .stream()
                .filter(violation -> matchesViolationFilter(violation, request))
                .toList();
    }

    private boolean matchesMonitoringFilter(MonitoringRecord record, FindMonitoringRecordsRequest request) {
        return matches(request.getProcessInstanceKey(), record.getProcessInstanceKey())
                && matches(request.getProcessDefinitionKey(), record.getProcessDefinitionKey())
                && matches(request.getBpmnProcessId(), record.getBpmnProcessId())
                && matches(request.getEvaluationStatus(), record.getEvaluationStatus());
    }

    private boolean matchesViolationFilter(ThresholdViolation violation, FindActiveViolationsRequest request) {
        return matches(request.getProcessDefinitionKey(), violation.getProcessDefinitionKey())
                && matches(request.getBpmnProcessId(), violation.getBpmnProcessId());
    }

    private boolean matches(Object filterValue, Object recordValue) {
        return filterValue == null || filterValue.equals(recordValue);
    }

    private MonitoringRecord calculationRecord(RecordCalculationRequest request, MonitoringRecord existing) {
        MonitoringRecord.MonitoringRecordBuilder builder = existing == null
                ? MonitoringRecord.builder()
                : existing.toBuilder();

        return builder
                .calculationEventId(request.getEventId())
                .calculationRequestId(request.getCalculationRequestId())
                .observationId(request.getObservationId())
                .sourceEventId(request.getSourceEventId())
                .processDefinitionKey(request.getProcessDefinitionKey())
                .bpmnProcessId(request.getBpmnProcessId())
                .processInstanceKey(request.getProcessInstanceKey())
                .elementInstanceKey(request.getElementInstanceKey())
                .bpmnElementId(request.getBpmnElementId())
                .keiId(request.getKeiId())
                .calculatedValue(request.getCalculatedValue())
                .calculatedUnit(request.getCalculatedUnit())
                .calculatedAt(request.getOccurredAt())
                .build();
    }

    private MonitoringRecord evaluationRecord(RecordEvaluationRequest request, MonitoringRecord existing) {
        MonitoringRecord.MonitoringRecordBuilder builder = existing == null
                ? MonitoringRecord.builder()
                : existing.toBuilder();

        return builder
                .calculationEventId(request.getCalculationResultId())
                .evaluationEventId(request.getEventId())
                .calculationRequestId(request.getCalculationRequestId())
                .observationId(request.getObservationId())
                .sourceEventId(request.getSourceEventId())
                .processDefinitionKey(request.getProcessDefinitionKey())
                .bpmnProcessId(request.getBpmnProcessId())
                .processInstanceKey(request.getProcessInstanceKey())
                .elementInstanceKey(request.getElementInstanceKey())
                .bpmnElementId(request.getBpmnElementId())
                .keiId(request.getKeiId())
                .calculatedValue(request.getCalculatedValue())
                .calculatedUnit(request.getCalculatedUnit())
                .targetValue(request.getTargetValue())
                .difference(request.getDifference())
                .evaluationStatus(request.getEvaluationStatus())
                .evaluatedAt(request.getOccurredAt())
                .build();
    }
}
