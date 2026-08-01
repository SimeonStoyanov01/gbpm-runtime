package cs.rug.monitoringresultsservice.application.mapper;

import cs.rug.monitoringresultsservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import org.springframework.stereotype.Component;

@Component
public class CalculationCompletedEventMapper {

    public MonitoringRecord toMonitoringRecord(KeiCalculationCompletedEvent event) {
        return MonitoringRecord
                .builder()
                .calculationEventId(event.getEventId())
                .engineType(event.getExecution().getEngineType())
                .calculatorId(event.getCalculation().getCalculatorId())
                .calculationMethod(event.getCalculation().getCalculationMethod())
                .referenceSetId(event.getCalculation().getReferenceSetId())
                .processDefinitionKey(event.getExecution().getProcessDefinitionKey())
                .bpmnProcessId(event.getExecution().getBpmnProcessId())
                .processInstanceKey(event.getExecution().getProcessInstanceKey())
                .elementInstanceKey(event.getExecution().getElementInstanceKey())
                .bpmnElementId(event.getExecution().getBpmnElementId())
                .workObjectType(event.getWorkObjectType())
                .keiId(event.getKei().getId())
                .calculatedValue(event.getResult().getValue())
                .calculatedUnit(event.getResult().getUnit())
                .calculatedAt(event.getOccurredAt())
                .resourceBreakdown(event.getResourceBreakdown())
                .build();
    }
}
