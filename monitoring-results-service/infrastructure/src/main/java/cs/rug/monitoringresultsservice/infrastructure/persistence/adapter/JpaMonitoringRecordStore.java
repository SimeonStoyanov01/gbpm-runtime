package cs.rug.monitoringresultsservice.infrastructure.persistence.adapter;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsRequest;
import cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails.FindProcessInstanceDetailsRequest;
import cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails.FindProcessInstanceDetailsResponse;
import cs.rug.monitoringresultsservice.api.operations.recordcalculation.RecordCalculationRequest;
import cs.rug.monitoringresultsservice.api.operations.recordevaluation.RecordEvaluationRequest;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiAnnotationEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiResultEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessDefinitionEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessInstanceEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ThresholdViolationEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.mapper.MonitoringResultEntityMapper;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.BpmnElementJpaRepository;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.KeiAnnotationJpaRepository;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.KeiResultJpaRepository;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.ProcessDefinitionJpaRepository;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.ProcessInstanceJpaRepository;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.ThresholdViolationJpaRepository;
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

    private static final String EVALUATION_STATUS_VIOLATED = "VIOLATED";

    private final ProcessDefinitionJpaRepository processDefinitionRepository;
    private final ProcessInstanceJpaRepository processInstanceRepository;
    private final BpmnElementJpaRepository bpmnElementRepository;
    private final KeiAnnotationJpaRepository keiAnnotationRepository;
    private final KeiResultJpaRepository keiResultRepository;
    private final ThresholdViolationJpaRepository thresholdViolationRepository;
    private final MonitoringResultEntityMapper monitoringResultMapper;

    @Override
    @Transactional
    public MonitoringRecord saveCalculation(RecordCalculationRequest request) {
        ProcessDefinitionEntity processDefinition = loadProcessDefinition(request.getProcessDefinitionKey());
        ProcessInstanceEntity processInstance = saveProcessInstance(request.getProcessInstanceKey(), processDefinition);
        BpmnElementEntity bpmnElement = loadBpmnElement(processDefinition, request.getBpmnElementId());
        KeiAnnotationEntity keiAnnotation = loadKeiAnnotation(bpmnElement, request.getKeiId());

        KeiResultEntity result = keiResultRepository
                .findByCalculationEventId(request.getEventId())
                .or(() -> findByExecution(processInstance, bpmnElement, keiAnnotation, request.getElementInstanceKey()))
                .orElseGet(this::newResult);

        applyCalculation(result, request, processInstance, bpmnElement, keiAnnotation);

        return monitoringResultMapper.toMonitoringRecord(keiResultRepository.save(result));
    }

    @Override
    @Transactional
    public MonitoringRecord saveEvaluation(RecordEvaluationRequest request) {
        ProcessDefinitionEntity processDefinition = loadProcessDefinition(request.getProcessDefinitionKey());
        ProcessInstanceEntity processInstance = saveProcessInstance(request.getProcessInstanceKey(), processDefinition);
        BpmnElementEntity bpmnElement = loadBpmnElement(processDefinition, request.getBpmnElementId());
        KeiAnnotationEntity keiAnnotation = loadKeiAnnotation(bpmnElement, request.getKeiId());

        KeiResultEntity result = keiResultRepository
                .findByEvaluationEventId(request.getEventId())
                .or(() -> keiResultRepository.findByCalculationEventId(request.getCalculationResultId()))
                .or(() -> findByExecution(processInstance, bpmnElement, keiAnnotation, request.getElementInstanceKey()))
                .orElseGet(this::newResult);

        applyEvaluation(result, request, processInstance, bpmnElement, keiAnnotation);
        KeiResultEntity savedResult = keiResultRepository.save(result);

        if (EVALUATION_STATUS_VIOLATED.equals(request.getEvaluationStatus())) {
            saveViolation(savedResult, request);
        }

        return monitoringResultMapper.toMonitoringRecord(savedResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitoringRecord> findMonitoringRecords(FindMonitoringRecordsRequest request) {
        return keiResultRepository
                .findAll()
                .stream()
                .filter(result -> matchesMonitoringFilter(result, request))
                .map(monitoringResultMapper::toMonitoringRecord)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FindProcessInstanceDetailsResponse findProcessInstanceDetails(FindProcessInstanceDetailsRequest request) {
        ProcessInstanceEntity processInstance = processInstanceRepository
                .findByProcessInstanceKey(request.getProcessInstanceKey())
                .orElseThrow(() -> new IllegalStateException(
                        "Process instance not found: " + request.getProcessInstanceKey()
                ));
        ProcessDefinitionEntity processDefinition = processInstance.getProcessDefinition();

        return FindProcessInstanceDetailsResponse
                .builder()
                .processDefinitionKey(processDefinition.getProcessDefinitionKey())
                .bpmnProcessId(processDefinition.getBpmnProcessId())
                .processInstanceKey(processInstance.getProcessInstanceKey())
                .bpmnXml(processDefinition.getBpmnXml())
                .records(keiResultRepository
                        .findByProcessInstanceProcessInstanceKey(processInstance.getProcessInstanceKey())
                        .stream()
                        .map(monitoringResultMapper::toMonitoringRecord)
                        .toList())
                .violations(thresholdViolationRepository
                        .findByKeiResultProcessInstanceProcessInstanceKey(processInstance.getProcessInstanceKey())
                        .stream()
                        .map(monitoringResultMapper::toThresholdViolation)
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

    private ThresholdViolationEntity newViolation() {
        Instant now = Instant.now();
        return ThresholdViolationEntity
                .builder()
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    private void saveViolation(
            KeiResultEntity result,
            RecordEvaluationRequest request
    ) {
        ThresholdViolationEntity entity = thresholdViolationRepository
                .findByEventId(request.getEventId())
                .orElseGet(this::newViolation);

        applyViolation(entity, result, request);
        thresholdViolationRepository.save(entity);
    }

    private void applyViolation(
            ThresholdViolationEntity entity,
            KeiResultEntity result,
            RecordEvaluationRequest request
    ) {
        entity.setEventId(request.getEventId());
        entity.setKeiResult(result);
        entity.setStatus(request.getEvaluationStatus());
        entity.setCalculatedValue(request.getCalculatedValue());
        entity.setTargetValue(request.getTargetValue());
        entity.setDifference(request.getDifference());
        entity.setOccurredAt(request.getOccurredAt());
        entity.setUpdatedAt(Instant.now());
    }

    private void applyCalculation(
            KeiResultEntity entity,
            RecordCalculationRequest request,
            ProcessInstanceEntity processInstance,
            BpmnElementEntity bpmnElement,
            KeiAnnotationEntity keiAnnotation
    ) {
        entity.setProcessInstance(processInstance);
        entity.setBpmnElement(bpmnElement);
        entity.setKeiAnnotation(keiAnnotation);
        entity.setElementInstanceKey(request.getElementInstanceKey());
        entity.setCalculationEventId(request.getEventId());
        entity.setCalculatedValue(request.getCalculatedValue());
        entity.setCalculatedUnit(request.getCalculatedUnit());
        entity.setCalculatedAt(request.getOccurredAt());
        entity.setUpdatedAt(Instant.now());
    }

    private void applyEvaluation(
            KeiResultEntity entity,
            RecordEvaluationRequest request,
            ProcessInstanceEntity processInstance,
            BpmnElementEntity bpmnElement,
            KeiAnnotationEntity keiAnnotation
    ) {
        entity.setProcessInstance(processInstance);
        entity.setBpmnElement(bpmnElement);
        entity.setKeiAnnotation(keiAnnotation);
        entity.setElementInstanceKey(request.getElementInstanceKey());
        entity.setCalculationEventId(request.getCalculationResultId());
        entity.setEvaluationEventId(request.getEventId());
        entity.setCalculatedValue(request.getCalculatedValue());
        entity.setCalculatedUnit(request.getCalculatedUnit());
        entity.setCalculatedAt(request.getCalculatedAt());
        entity.setTargetValue(request.getTargetValue());
        entity.setDifference(request.getDifference());
        entity.setEvaluationStatus(request.getEvaluationStatus());
        entity.setEvaluatedAt(request.getOccurredAt());
        entity.setUpdatedAt(Instant.now());
    }

    private boolean matchesMonitoringFilter(KeiResultEntity result, FindMonitoringRecordsRequest request) {
        return matches(request.getProcessInstanceKey(), result.getProcessInstance().getProcessInstanceKey())
                && matches(
                        request.getProcessDefinitionKey(),
                        result.getProcessInstance().getProcessDefinition().getProcessDefinitionKey()
                )
                && matches(
                        request.getBpmnProcessId(),
                        result.getProcessInstance().getProcessDefinition().getBpmnProcessId()
                )
                && matches(request.getEvaluationStatus(), result.getEvaluationStatus());
    }

    private boolean matches(Object filterValue, Object recordValue) {
        return filterValue == null || filterValue.equals(recordValue);
    }
}
