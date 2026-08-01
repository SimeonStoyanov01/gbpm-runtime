package cs.rug.monitoringresultsservice.infrastructure.persistence.adapter;

import cs.rug.monitoringresultsservice.api.exceptions.ProcessInstanceNotFoundException;
import cs.rug.monitoringresultsservice.api.model.EvaluationStatus;
import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.model.ResourceBreakdown;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsRequest;
import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsRequest;
import cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails.FindProcessInstanceDetailsRequest;
import cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails.FindProcessInstanceDetailsResponse;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiAnnotationEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiResultEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiResourceBreakdownEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessDefinitionEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessInstanceEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.mapper.MonitoringResultEntityMapper;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.BpmnElementJpaRepository;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.KeiAnnotationJpaRepository;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.KeiResultJpaRepository;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.ProcessDefinitionJpaRepository;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.ProcessInstanceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaMonitoringRecordStore implements MonitoringRecordStore {

    private final ProcessDefinitionJpaRepository processDefinitionRepository;
    private final ProcessInstanceJpaRepository processInstanceRepository;
    private final BpmnElementJpaRepository bpmnElementRepository;
    private final KeiAnnotationJpaRepository keiAnnotationRepository;
    private final KeiResultJpaRepository keiResultRepository;
    private final MonitoringResultEntityMapper monitoringResultMapper;

    @Override
    @Transactional
    public MonitoringRecord saveCalculation(MonitoringRecord record) {
        ProcessDefinitionEntity processDefinition = loadProcessDefinition(record.getProcessDefinitionKey());
        ProcessInstanceEntity processInstance = saveProcessInstance(record.getProcessInstanceKey(), processDefinition);
        BpmnElementEntity bpmnElement = loadBpmnElement(processDefinition, record.getBpmnElementId());
        KeiAnnotationEntity keiAnnotation = loadKeiAnnotation(bpmnElement, record.getKeiId());

        KeiResultEntity result = keiResultRepository
                .findByCalculationEventId(record.getCalculationEventId())
                .or(() -> findByExecution(processInstance, bpmnElement, keiAnnotation, record.getElementInstanceKey()))
                .orElseGet(this::newResult);

        applyCalculation(result, record, processInstance, bpmnElement, keiAnnotation);

        return monitoringResultMapper.toMonitoringRecord(keiResultRepository.save(result));
    }

    @Override
    @Transactional
    public MonitoringRecord saveEvaluation(MonitoringRecord record) {
        ProcessDefinitionEntity processDefinition = loadProcessDefinition(record.getProcessDefinitionKey());
        ProcessInstanceEntity processInstance = saveProcessInstance(record.getProcessInstanceKey(), processDefinition);
        BpmnElementEntity bpmnElement = loadBpmnElement(processDefinition, record.getBpmnElementId());
        KeiAnnotationEntity keiAnnotation = loadKeiAnnotation(bpmnElement, record.getKeiId());

        KeiResultEntity result = keiResultRepository
                .findByEvaluationEventId(record.getEvaluationEventId())
                .or(() -> keiResultRepository.findByCalculationEventId(record.getCalculationEventId()))
                .or(() -> findByExecution(processInstance, bpmnElement, keiAnnotation, record.getElementInstanceKey()))
                .orElseGet(this::newResult);

        applyEvaluation(result, record, processInstance, bpmnElement, keiAnnotation);
        KeiResultEntity savedResult = keiResultRepository.save(result);

        return monitoringResultMapper.toMonitoringRecord(savedResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitoringRecord> findMonitoringRecords(FindMonitoringRecordsRequest request) {
        return keiResultRepository
                .findAllByFilters(
                        request.getProcessInstanceKey(),
                        request.getProcessDefinitionKey(),
                        request.getBpmnProcessId(),
                        request.getEvaluationStatus()
                )
                .stream()
                .map(monitoringResultMapper::toMonitoringRecord)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitoringRecord> findActiveViolations(FindActiveViolationsRequest request) {
        return keiResultRepository
                .findAllByFilters(
                        null,
                        request.getProcessDefinitionKey(),
                        request.getBpmnProcessId(),
                        EvaluationStatus.VIOLATED.name()
                )
                .stream()
                .map(monitoringResultMapper::toMonitoringRecord)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FindProcessInstanceDetailsResponse findProcessInstanceDetails(FindProcessInstanceDetailsRequest request) {
        ProcessInstanceEntity processInstance = processInstanceRepository
                .findByProcessInstanceKey(request.getProcessInstanceKey())
                .orElseThrow(() -> new ProcessInstanceNotFoundException(request.getProcessInstanceKey()));
        ProcessDefinitionEntity processDefinition = processInstance.getProcessDefinition();
        List<MonitoringRecord> records = keiResultRepository
                .findAllByFilters(
                        processInstance.getProcessInstanceKey(),
                        null,
                        null,
                        null
                )
                .stream()
                .map(monitoringResultMapper::toMonitoringRecord)
                .toList();

        return FindProcessInstanceDetailsResponse
                .builder()
                .processDefinitionKey(processDefinition.getProcessDefinitionKey())
                .bpmnProcessId(processDefinition.getBpmnProcessId())
                .resourceName(processDefinition.getResourceName())
                .processInstanceKey(processInstance.getProcessInstanceKey())
                .bpmnXml(processDefinition.getBpmnXml())
                .records(records)
                .violations(records
                        .stream()
                        .filter(record -> EvaluationStatus.VIOLATED.name().equals(record.getEvaluationStatus()))
                        .toList())
                .build();
    }

    private ProcessDefinitionEntity loadProcessDefinition(Long processDefinitionKey) {
        return processDefinitionRepository
                .findByProcessDefinitionKey(processDefinitionKey)
                .orElseThrow(() -> new IllegalStateException(
                        "No registered process definition found for result: " + processDefinitionKey
                ));
    }

    private ProcessInstanceEntity saveProcessInstance(
            Long processInstanceKey,
            ProcessDefinitionEntity processDefinition
    ) {
        processInstanceRepository.saveProcessInstance(UUID.randomUUID(), processInstanceKey, processDefinition.getId());
        return processInstanceRepository
                .findByProcessInstanceKey(processInstanceKey)
                .orElseThrow(() -> new IllegalStateException(
                        "Process instance save did not return a row: " + processInstanceKey
                ));
    }

    private BpmnElementEntity loadBpmnElement(ProcessDefinitionEntity processDefinition, String bpmnElementId) {
        return bpmnElementRepository
                .findByProcessDefinitionAndBpmnElementId(processDefinition, bpmnElementId)
                .orElseThrow(() -> new IllegalStateException(
                        "No registered BPMN element found for result: " + bpmnElementId
                ));
    }

    private KeiAnnotationEntity loadKeiAnnotation(BpmnElementEntity bpmnElement, String keiId) {
        return keiAnnotationRepository
                .findByBpmnElementAndKeiId(bpmnElement, keiId)
                .orElseThrow(() -> new IllegalStateException(
                        "No registered KEI annotation found for result: " + keiId
                ));
    }

    private Optional<KeiResultEntity> findByExecution(
            ProcessInstanceEntity processInstance,
            BpmnElementEntity bpmnElement,
            KeiAnnotationEntity keiAnnotation,
            Long elementInstanceKey
    ) {
        return keiResultRepository.findByProcessInstanceAndBpmnElementAndKeiAnnotationAndElementInstanceKey(
                processInstance,
                bpmnElement,
                keiAnnotation,
                elementInstanceKey
        );
    }

    private KeiResultEntity newResult() {
        Instant now = Instant.now();
        return KeiResultEntity
                .builder()
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    private void applyCalculation(
            KeiResultEntity entity,
            MonitoringRecord record,
            ProcessInstanceEntity processInstance,
            BpmnElementEntity bpmnElement,
            KeiAnnotationEntity keiAnnotation
    ) {
        entity.setProcessInstance(processInstance);
        entity.setBpmnElement(bpmnElement);
        entity.setKeiAnnotation(keiAnnotation);
        entity.setElementInstanceKey(record.getElementInstanceKey());
        entity.setEngineType(record.getEngineType());
        entity.setCalculatorId(record.getCalculatorId());
        entity.setCalculationMethod(record.getCalculationMethod());
        entity.setReferenceSetId(record.getReferenceSetId());
        entity.setWorkObjectType(record.getWorkObjectType());
        entity.setCalculationEventId(record.getCalculationEventId());
        entity.setCalculatedValue(record.getCalculatedValue());
        entity.setCalculatedUnit(record.getCalculatedUnit());
        entity.setCalculatedAt(record.getCalculatedAt());
        replaceResourceBreakdown(entity, record.getResourceBreakdown());
        entity.setUpdatedAt(Instant.now());
    }

    private void applyEvaluation(
            KeiResultEntity entity,
            MonitoringRecord record,
            ProcessInstanceEntity processInstance,
            BpmnElementEntity bpmnElement,
            KeiAnnotationEntity keiAnnotation
    ) {
        entity.setProcessInstance(processInstance);
        entity.setBpmnElement(bpmnElement);
        entity.setKeiAnnotation(keiAnnotation);
        entity.setElementInstanceKey(record.getElementInstanceKey());
        entity.setEngineType(record.getEngineType());
        entity.setCalculatorId(record.getCalculatorId());
        entity.setCalculationMethod(record.getCalculationMethod());
        entity.setReferenceSetId(record.getReferenceSetId());
        entity.setWorkObjectType(record.getWorkObjectType());
        entity.setCalculationEventId(record.getCalculationEventId());
        entity.setEvaluationEventId(record.getEvaluationEventId());
        entity.setCalculatedValue(record.getCalculatedValue());
        entity.setCalculatedUnit(record.getCalculatedUnit());
        entity.setCalculatedAt(record.getCalculatedAt());
        entity.setTargetValue(record.getTargetValue());
        entity.setDifference(record.getDifference());
        entity.setEvaluationStatus(record.getEvaluationStatus());
        entity.setEvaluatedAt(record.getEvaluatedAt());
        replaceResourceBreakdown(entity, record.getResourceBreakdown());
        entity.setUpdatedAt(Instant.now());
    }

    private void replaceResourceBreakdown(
            KeiResultEntity result,
            List<ResourceBreakdown> resourceBreakdown
    ) {
        if (resourceBreakdown == null) {
            return;
        }

        result.getResourceBreakdown().clear();
        result.getResourceBreakdown().addAll(resourceBreakdown
                .stream()
                .map(resource -> KeiResourceBreakdownEntity
                        .builder()
                        .keiResult(result)
                        .resourceName(resource.getResourceName())
                        .usageValue(resource.getUsageValue())
                        .usageUnit(resource.getUsageUnit())
                        .emissionValue(resource.getEmissionValue())
                        .unit(resource.getUnit())
                        .build())
                .toList());
    }

}
