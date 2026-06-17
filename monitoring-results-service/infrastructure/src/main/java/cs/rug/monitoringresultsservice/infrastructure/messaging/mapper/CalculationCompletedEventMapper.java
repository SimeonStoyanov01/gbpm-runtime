package cs.rug.monitoringresultsservice.infrastructure.messaging.mapper;

import cs.rug.monitoringresultsservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.monitoringresultsservice.api.operations.recordcalculation.RecordCalculationRequest;
import org.springframework.stereotype.Component;

@Component
public class CalculationCompletedEventMapper {

    public RecordCalculationRequest toRequest(KeiCalculationCompletedEvent event) {
        return RecordCalculationRequest
                .builder()
                .eventId(event.getEventId())
                .calculationRequestId(event.getCalculationRequestId())
                .observationId(event.getObservationId())
                .sourceEventId(event.getSourceEventId())
                .occurredAt(event.getOccurredAt())
                .processDefinitionKey(event.getExecution().getProcessDefinitionKey())
                .bpmnProcessId(event.getExecution().getBpmnProcessId())
                .processInstanceKey(event.getExecution().getProcessInstanceKey())
                .elementInstanceKey(event.getExecution().getElementInstanceKey())
                .bpmnElementId(event.getExecution().getBpmnElementId())
                .keiId(event.getKei().getId())
                .calculatedValue(event.getResult().getValue())
                .calculatedUnit(event.getResult().getUnit())
                .build();
    }
}
