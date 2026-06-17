package cs.rug.monitoringresultsservice.application.recordviolation;

import cs.rug.monitoringresultsservice.api.model.ThresholdViolation;
import cs.rug.monitoringresultsservice.api.operations.recordviolation.RecordViolationOperation;
import cs.rug.monitoringresultsservice.api.operations.recordviolation.RecordViolationRequest;
import cs.rug.monitoringresultsservice.api.operations.recordviolation.RecordViolationResponse;
import cs.rug.monitoringresultsservice.application.out.MonitoringUpdatePublisher;
import cs.rug.monitoringresultsservice.application.out.ThresholdViolationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordViolationProcessor implements RecordViolationOperation {

    private final ThresholdViolationStore thresholdViolationStore;
    private final MonitoringUpdatePublisher monitoringUpdatePublisher;

    @Override
    public RecordViolationResponse process(RecordViolationRequest request) {
        ThresholdViolation violation = ThresholdViolation
                .builder()
                .eventId(request.getEventId())
                .occurredAt(request.getOccurredAt())
                .processDefinitionKey(request.getProcessDefinitionKey())
                .bpmnProcessId(request.getBpmnProcessId())
                .serviceTaskId(request.getServiceTaskId())
                .processInstanceKey(request.getProcessInstanceKey())
                .emissionType(request.getEmissionType())
                .calculatedValue(request.getCalculatedValue())
                .targetValue(request.getTargetValue())
                .difference(request.getDifference())
                .status(request.getStatus())
                .build();

        ThresholdViolation activeViolation = thresholdViolationStore.saveOrUpdateActiveViolation(violation);
        monitoringUpdatePublisher.publishViolationUpdate(activeViolation);

        return RecordViolationResponse
                .builder()
                .violation(activeViolation)
                .build();
    }
}
