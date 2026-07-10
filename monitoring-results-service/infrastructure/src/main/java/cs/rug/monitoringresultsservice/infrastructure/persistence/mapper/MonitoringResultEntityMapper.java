package cs.rug.monitoringresultsservice.infrastructure.persistence.mapper;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.model.ThresholdViolation;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiAnnotationEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiResultEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessDefinitionEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessInstanceEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ThresholdViolationEntity;
import org.springframework.stereotype.Component;

@Component
public class MonitoringResultEntityMapper {

    public MonitoringRecord toMonitoringRecord(KeiResultEntity entity) {
        ProcessInstanceEntity processInstance = entity.getProcessInstance();
        ProcessDefinitionEntity processDefinition = processInstance.getProcessDefinition();
        BpmnElementEntity bpmnElement = entity.getBpmnElement();
        KeiAnnotationEntity keiAnnotation = entity.getKeiAnnotation();

        return MonitoringRecord
                .builder()
                .calculationEventId(entity.getCalculationEventId())
                .evaluationEventId(entity.getEvaluationEventId())
                .processDefinitionKey(processDefinition.getProcessDefinitionKey())
                .bpmnProcessId(processDefinition.getBpmnProcessId())
                .processInstanceKey(processInstance.getProcessInstanceKey())
                .elementInstanceKey(entity.getElementInstanceKey())
                .bpmnElementId(bpmnElement.getBpmnElementId())
                .keiId(keiAnnotation.getKeiId())
                .calculatedValue(entity.getCalculatedValue())
                .calculatedUnit(entity.getCalculatedUnit())
                .targetValue(entity.getTargetValue())
                .difference(entity.getDifference())
                .evaluationStatus(entity.getEvaluationStatus())
                .calculatedAt(entity.getCalculatedAt())
                .evaluatedAt(entity.getEvaluatedAt())
                .build();
    }

    public ThresholdViolation toThresholdViolation(ThresholdViolationEntity entity) {
        KeiResultEntity result = entity.getKeiResult();
        ProcessInstanceEntity processInstance = result.getProcessInstance();
        ProcessDefinitionEntity processDefinition = processInstance.getProcessDefinition();

        return ThresholdViolation
                .builder()
                .eventId(entity.getEventId())
                .processDefinitionKey(processDefinition.getProcessDefinitionKey())
                .bpmnProcessId(processDefinition.getBpmnProcessId())
                .serviceTaskId(result.getBpmnElement().getBpmnElementId())
                .processInstanceKey(processInstance.getProcessInstanceKey())
                .emissionType(result.getKeiAnnotation().getKeiId())
                .calculatedValue(entity.getCalculatedValue())
                .targetValue(entity.getTargetValue())
                .difference(entity.getDifference())
                .status(entity.getStatus())
                .occurredAt(entity.getOccurredAt())
                .build();
    }
}
