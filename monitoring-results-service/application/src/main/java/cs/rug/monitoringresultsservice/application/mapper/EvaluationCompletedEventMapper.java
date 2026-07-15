package cs.rug.monitoringresultsservice.application.mapper;

import cs.rug.monitoringresultsservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.monitoringresultsservice.api.events.evaluationcompleted.KeiEvaluationCompletedEvent;
import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import org.springframework.stereotype.Component;

@Component
public class EvaluationCompletedEventMapper {

    public MonitoringRecord toMonitoringRecord(KeiEvaluationCompletedEvent event) {
        KeiCalculationCompletedEvent calculationEvent = event.getCalculationEvent();

        return MonitoringRecord
                .builder()
                .evaluationEventId(event.getEventId())
                .calculationEventId(calculationEvent.getEventId())
                .engineType(calculationEvent.getExecution().getEngineType())
                .calculatorId(calculationEvent.getCalculation().getCalculatorId())
                .calculationMethod(calculationEvent.getCalculation().getCalculationMethod())
                .referenceSetId(calculationEvent.getCalculation().getReferenceSetId())
                .processDefinitionKey(calculationEvent.getExecution().getProcessDefinitionKey())
                .bpmnProcessId(calculationEvent.getExecution().getBpmnProcessId())
                .processInstanceKey(calculationEvent.getExecution().getProcessInstanceKey())
                .elementInstanceKey(calculationEvent.getExecution().getElementInstanceKey())
                .bpmnElementId(calculationEvent.getExecution().getBpmnElementId())
                .keiId(calculationEvent.getKei().getId())
                .calculatedValue(calculationEvent.getResult().getValue())
                .calculatedUnit(calculationEvent.getResult().getUnit())
                .calculatedAt(calculationEvent.getOccurredAt())
                .targetValue(event.getEvaluation().getTargetValue())
                .difference(event.getEvaluation().getDifference())
                .evaluationStatus(event.getEvaluation().getStatus())
                .evaluatedAt(event.getOccurredAt())
                .resourceBreakdown(calculationEvent.getResourceBreakdown())
                .build();
    }
}
