package cs.rug.monitoringresultsservice.infrastructure.persistence.mapper;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiAnnotationEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiResultEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessDefinitionEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessInstanceEntity;
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
                .engineType(entity.getEngineType())
                .calculatorId(entity.getCalculatorId())
                .calculationMethod(entity.getCalculationMethod())
                .referenceSetId(entity.getReferenceSetId())
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
}
