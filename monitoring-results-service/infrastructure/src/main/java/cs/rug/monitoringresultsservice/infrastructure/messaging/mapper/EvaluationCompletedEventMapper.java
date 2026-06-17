package cs.rug.monitoringresultsservice.infrastructure.messaging.mapper;

import cs.rug.monitoringresultsservice.api.events.evaluationcompleted.KeiEvaluationCompletedEvent;
import cs.rug.monitoringresultsservice.api.operations.recordevaluation.RecordEvaluationRequest;
import org.springframework.stereotype.Component;

@Component
public class EvaluationCompletedEventMapper {

    public RecordEvaluationRequest toRequest(KeiEvaluationCompletedEvent event) {
        return RecordEvaluationRequest
                .builder()
                .eventId(event.getEventId())
                .calculationResultId(event.getCalculationResultId())
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
                .keiName(event.getKei().getName())
                .keiUnit(event.getKei().getUnit())
                .keiTargetValue(event.getKei().getTargetValue())
                .calculatedValue(event.getCalculatedResult().getValue())
                .calculatedUnit(event.getCalculatedResult().getUnit())
                .calculatedAt(event.getCalculatedResult().getCalculatedAt())
                .targetValue(event.getEvaluation().getTargetValue())
                .difference(event.getEvaluation().getDifference())
                .evaluationStatus(event.getEvaluation().getStatus())
                .build();
    }
}
