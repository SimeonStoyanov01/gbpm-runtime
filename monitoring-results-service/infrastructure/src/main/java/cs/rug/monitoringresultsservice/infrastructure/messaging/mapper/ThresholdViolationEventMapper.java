package cs.rug.monitoringresultsservice.infrastructure.messaging.mapper;

import cs.rug.monitoringresultsservice.api.events.thresholdviolationdetected.ThresholdViolationDetectedEvent;
import cs.rug.monitoringresultsservice.api.operations.recordviolation.RecordViolationRequest;
import org.springframework.stereotype.Component;

@Component
public class ThresholdViolationEventMapper {

    public RecordViolationRequest toRequest(ThresholdViolationDetectedEvent event) {
        return RecordViolationRequest
                .builder()
                .eventId(event.getEventId())
                .occurredAt(event.getOccurredAt())
                .processDefinitionKey(event.getProcessDefinitionKey())
                .bpmnProcessId(event.getBpmnProcessId())
                .serviceTaskId(event.getServiceTaskId())
                .processInstanceKey(event.getProcessInstanceKey())
                .emissionType(event.getEmissionType())
                .calculatedValue(event.getCalculatedValue())
                .targetValue(event.getTargetValue())
                .difference(event.getDifference())
                .status(event.getStatus())
                .build();
    }
}
