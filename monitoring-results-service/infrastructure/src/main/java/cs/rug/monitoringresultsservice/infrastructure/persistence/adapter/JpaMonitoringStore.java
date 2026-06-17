package cs.rug.monitoringresultsservice.infrastructure.persistence.adapter;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.model.ThresholdViolation;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsRequest;
import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsRequest;
import cs.rug.monitoringresultsservice.api.operations.recordcalculation.RecordCalculationRequest;
import cs.rug.monitoringresultsservice.api.operations.recordevaluation.RecordEvaluationRequest;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import cs.rug.monitoringresultsservice.application.out.ThresholdViolationStore;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiAnnotationEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiResultEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessDefinitionEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessInstanceEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ThresholdViolationEntity;
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

@Component
@RequiredArgsConstructor
public class JpaMonitoringStore implements MonitoringRecordStore, ThresholdViolationStore {

    private final ProcessDefinitionJpaRepository processDefinitionRepository;
    private final ProcessInstanceJpaRepository processInstanceRepository;
    private final BpmnElementJpaRepository bpmnElementRepository;
    private final KeiAnnotationJpaRepository keiAnnotationRepository;
    private final KeiResultJpaRepository keiResultRepository;
    private final ThresholdViolationJpaRepository thresholdViolationRepository;

    @Override
    @Transactional
    public MonitoringRecord saveCalculation(RecordCalculationRequest request) {
        ProcessDefinitionEntity processDefinition = processDefinition(
                request.getProcessDefinitionKey(),
                request.getBpmnProcessId()
        );
        ProcessInstanceEntity processInstance = processInstance(request.getProcessInstanceKey(), processDefinition);
        BpmnElementEntity bpmnElement = bpmnElement(processDefinition, request.getBpmnElementId());
        KeiAnnotationEntity keiAnnotation = keiAnnotation(
                bpmnElement,
                request.getKeiId(),
                request.getKeiName(),
                request.getKeiUnit(),
                request.getKeiTargetValue()
        );

        KeiResultEntity result = keiResultRepository
                .findByCalculationEventId(request.getEventId())
                .or(() -> findByExecution(processInstance, bpmnElement, keiAnnotation, request.getElementInstanceKey()))
                .orElseGet(this::newResult);

        result.setProcessInstance(processInstance);
        result.setBpmnElement(bpmnElement);
        result.setKeiAnnotation(keiAnnotation);
        result.setElementInstanceKey(request.getElementInstanceKey());
        result.setCalculationEventId(request.getEventId());
        result.setCalculationRequestId(request.getCalculationRequestId());
        result.setObservationId(request.getObservationId());
        result.setSourceEventId(request.getSourceEventId());
        result.setCalculatedValue(request.getCalculatedValue());
        result.setCalculatedUnit(request.getCalculatedUnit());
        result.setCalculatedAt(request.getOccurredAt());
        result.setUpdatedAt(Instant.now());

        return toMonitoringRecord(keiResultRepository.save(result));
    }

    @Override
    @Transactional
    public MonitoringRecord saveEvaluation(RecordEvaluationRequest request) {
        ProcessDefinitionEntity processDefinition = processDefinition(
                request.getProcessDefinitionKey(),
                request.getBpmnProcessId()
        );
        ProcessInstanceEntity processInstance = processInstance(request.getProcessInstanceKey(), processDefinition);
        BpmnElementEntity bpmnElement = bpmnElement(processDefinition, request.getBpmnElementId());
        KeiAnnotationEntity keiAnnotation = keiAnnotation(
                bpmnElement,
                request.getKeiId(),
                request.getKeiName(),
                request.getKeiUnit(),
                request.getKeiTargetValue()
        );

        KeiResultEntity result = keiResultRepository
                .findByEvaluationEventId(request.getEventId())
                .or(() -> keiResultRepository.findByCalculationEventId(request.getCalculationResultId()))
                .or(() -> findByExecution(processInstance, bpmnElement, keiAnnotation, request.getElementInstanceKey()))
                .orElseGet(this::newResult);

        result.setProcessInstance(processInstance);
        result.setBpmnElement(bpmnElement);
        result.setKeiAnnotation(keiAnnotation);
        result.setElementInstanceKey(request.getElementInstanceKey());
        result.setCalculationEventId(request.getCalculationResultId());
        result.setEvaluationEventId(request.getEventId());
        result.setCalculationRequestId(request.getCalculationRequestId());
        result.setObservationId(request.getObservationId());
        result.setSourceEventId(request.getSourceEventId());
        result.setCalculatedValue(request.getCalculatedValue());
        result.setCalculatedUnit(request.getCalculatedUnit());
        result.setTargetValue(request.getTargetValue());
        result.setDifference(request.getDifference());
        result.setEvaluationStatus(request.getEvaluationStatus());
        result.setEvaluatedAt(request.getOccurredAt());
        result.setUpdatedAt(Instant.now());

        return toMonitoringRecord(keiResultRepository.save(result));
    }

    @Override
    @Transactional
    public void saveViolationMarker(ThresholdViolation violation) {
        findResultForViolation(violation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitoringRecord> findMonitoringRecords(FindMonitoringRecordsRequest request) {
        return keiResultRepository
                .findAll()
                .stream()
                .filter(result -> matchesMonitoringFilter(result, request))
                .map(this::toMonitoringRecord)
                .toList();
    }

    @Override
    @Transactional
    public ThresholdViolation saveOrUpdateActiveViolation(ThresholdViolation violation) {
        ThresholdViolationEntity entity = thresholdViolationRepository
                .findByEventId(violation.getEventId())
                .orElseGet(this::newViolation);

        KeiResultEntity result = findResultForViolation(violation);

        entity.setEventId(violation.getEventId());
        entity.setKeiResult(result);
        entity.setStatus(violation.getStatus());
        entity.setCalculatedValue(violation.getCalculatedValue());
        entity.setTargetValue(violation.getTargetValue());
        entity.setDifference(violation.getDifference());
        entity.setOccurredAt(violation.getOccurredAt());
        entity.setUpdatedAt(Instant.now());

        return toThresholdViolation(thresholdViolationRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThresholdViolation> findActiveViolations(FindActiveViolationsRequest request) {
        return thresholdViolationRepository
                .findAll()
                .stream()
                .filter(violation -> matchesViolationFilter(violation, request))
                .map(this::toThresholdViolation)
                .toList();
    }

    private ProcessDefinitionEntity processDefinition(Long processDefinitionKey, String bpmnProcessId) {
        ProcessDefinitionEntity entity = processDefinitionRepository
                .findByProcessDefinitionKey(processDefinitionKey)
                .orElseGet(ProcessDefinitionEntity::new);

        entity.setProcessDefinitionKey(processDefinitionKey);
        entity.setBpmnProcessId(bpmnProcessId);
        return processDefinitionRepository.save(entity);
    }

    private ProcessInstanceEntity processInstance(
            Long processInstanceKey,
            ProcessDefinitionEntity processDefinition
    ) {
        ProcessInstanceEntity entity = processInstanceRepository
                .findByProcessInstanceKey(processInstanceKey)
                .orElseGet(ProcessInstanceEntity::new);

        entity.setProcessInstanceKey(processInstanceKey);
        entity.setProcessDefinition(processDefinition);
        return processInstanceRepository.save(entity);
    }

    private BpmnElementEntity bpmnElement(ProcessDefinitionEntity processDefinition, String bpmnElementId) {
        BpmnElementEntity entity = bpmnElementRepository
                .findByProcessDefinitionAndBpmnElementId(processDefinition, bpmnElementId)
                .orElseGet(BpmnElementEntity::new);

        entity.setProcessDefinition(processDefinition);
        entity.setBpmnElementId(bpmnElementId);
        return bpmnElementRepository.save(entity);
    }

    private KeiAnnotationEntity keiAnnotation(
            BpmnElementEntity bpmnElement,
            String keiId,
            String name,
            String unit,
            String targetValue
    ) {
        KeiAnnotationEntity entity = keiAnnotationRepository
                .findByBpmnElementAndKeiId(bpmnElement, keiId)
                .orElseGet(KeiAnnotationEntity::new);

        entity.setBpmnElement(bpmnElement);
        entity.setKeiId(keiId);
        entity.setName(name);
        entity.setUnit(unit);
        entity.setTargetValue(targetValue);
        return keiAnnotationRepository.save(entity);
    }

    private java.util.Optional<KeiResultEntity> findByExecution(
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

    private KeiResultEntity findResultForViolation(ThresholdViolation violation) {
        ProcessDefinitionEntity processDefinition = processDefinition(
                violation.getProcessDefinitionKey(),
                violation.getBpmnProcessId()
        );
        ProcessInstanceEntity processInstance = processInstance(violation.getProcessInstanceKey(), processDefinition);
        BpmnElementEntity bpmnElement = bpmnElement(processDefinition, violation.getServiceTaskId());
        KeiAnnotationEntity keiAnnotation = keiAnnotation(
                bpmnElement,
                violation.getEmissionType(),
                null,
                null,
                null
        );

        return keiResultRepository
                .findFirstByProcessInstanceAndBpmnElementAndKeiAnnotationOrderByEvaluatedAtDesc(
                        processInstance,
                        bpmnElement,
                        keiAnnotation
                )
                .orElseGet(() -> {
                    KeiResultEntity result = newResult();
                    result.setProcessInstance(processInstance);
                    result.setBpmnElement(bpmnElement);
                    result.setKeiAnnotation(keiAnnotation);
                    result.setCalculatedValue(violation.getCalculatedValue());
                    result.setTargetValue(violation.getTargetValue());
                    result.setDifference(violation.getDifference());
                    result.setEvaluationStatus(violation.getStatus());
                    result.setEvaluatedAt(violation.getOccurredAt());
                    result.setUpdatedAt(Instant.now());
                    return keiResultRepository.save(result);
                });
    }

    private KeiResultEntity newResult() {
        KeiResultEntity entity = new KeiResultEntity();
        Instant now = Instant.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return entity;
    }

    private ThresholdViolationEntity newViolation() {
        ThresholdViolationEntity entity = new ThresholdViolationEntity();
        Instant now = Instant.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return entity;
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

    private boolean matchesViolationFilter(ThresholdViolationEntity violation, FindActiveViolationsRequest request) {
        KeiResultEntity result = violation.getKeiResult();
        return result != null
                && matches(
                        request.getProcessDefinitionKey(),
                        result.getProcessInstance().getProcessDefinition().getProcessDefinitionKey()
                )
                && matches(
                        request.getBpmnProcessId(),
                        result.getProcessInstance().getProcessDefinition().getBpmnProcessId()
                );
    }

    private boolean matches(Object filterValue, Object recordValue) {
        return filterValue == null || filterValue.equals(recordValue);
    }

    private MonitoringRecord toMonitoringRecord(KeiResultEntity entity) {
        ProcessInstanceEntity processInstance = entity.getProcessInstance();
        ProcessDefinitionEntity processDefinition = processInstance.getProcessDefinition();
        BpmnElementEntity bpmnElement = entity.getBpmnElement();
        KeiAnnotationEntity keiAnnotation = entity.getKeiAnnotation();

        return MonitoringRecord
                .builder()
                .calculationEventId(entity.getCalculationEventId())
                .evaluationEventId(entity.getEvaluationEventId())
                .calculationRequestId(entity.getCalculationRequestId())
                .observationId(entity.getObservationId())
                .sourceEventId(entity.getSourceEventId())
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

    private ThresholdViolation toThresholdViolation(ThresholdViolationEntity entity) {
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
