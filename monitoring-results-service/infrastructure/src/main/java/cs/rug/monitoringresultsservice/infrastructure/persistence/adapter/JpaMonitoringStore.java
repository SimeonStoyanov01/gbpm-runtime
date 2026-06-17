package cs.rug.monitoringresultsservice.infrastructure.persistence.adapter;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.model.ThresholdViolation;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsRequest;
import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsRequest;
import cs.rug.monitoringresultsservice.api.operations.recordcalculation.RecordCalculationRequest;
import cs.rug.monitoringresultsservice.api.operations.recordevaluation.RecordEvaluationRequest;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.ProcessModelElement;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.ProcessModelKeiAnnotation;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelRequest;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelResponse;
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
import java.util.Optional;

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

        applyCalculation(result, request, processInstance, bpmnElement, keiAnnotation);

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

        applyEvaluation(result, request, processInstance, bpmnElement, keiAnnotation);

        return toMonitoringRecord(keiResultRepository.save(result));
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
    public RegisterProcessModelResponse registerProcessModel(RegisterProcessModelRequest request) {
        ProcessDefinitionEntity processDefinition = processDefinition(
                request.getProcessDefinitionKey(),
                request.getBpmnProcessId()
        );
        applyProcessModel(processDefinition, request);
        processDefinitionRepository.save(processDefinition);

        int annotationCount = 0;
        if (request.getElements() != null) {
            for (ProcessModelElement element : request.getElements()) {
                BpmnElementEntity bpmnElement = bpmnElement(processDefinition, element.getBpmnElementId());
                applyProcessModelElement(bpmnElement, element);
                bpmnElementRepository.save(bpmnElement);

                if (element.getKeiAnnotations() != null) {
                    for (ProcessModelKeiAnnotation annotation : element.getKeiAnnotations()) {
                        KeiAnnotationEntity keiAnnotation = keiAnnotation(
                                bpmnElement,
                                annotation.getId(),
                                annotation.getName(),
                                annotation.getUnit(),
                                annotation.getTargetValue()
                        );
                        applyProcessModelKeiAnnotation(keiAnnotation, annotation);
                        keiAnnotationRepository.save(keiAnnotation);
                        annotationCount++;
                    }
                }
            }
        }

        return RegisterProcessModelResponse
                .builder()
                .processDefinitionKey(request.getProcessDefinitionKey())
                .elementCount(request.getElements() == null ? 0 : request.getElements().size())
                .keiAnnotationCount(annotationCount)
                .build();
    }

    @Override
    @Transactional
    public ThresholdViolation saveOrUpdateActiveViolation(ThresholdViolation violation) {
        ThresholdViolationEntity entity = thresholdViolationRepository
                .findByEventId(violation.getEventId())
                .orElseGet(this::newViolation);

        Optional<KeiResultEntity> result = findResultForViolation(violation);
        if (result.isEmpty()) {
            return violation;
        }

        applyViolation(entity, violation, result.get());

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
        processDefinitionRepository.upsert(processDefinitionKey, bpmnProcessId);
        return processDefinitionRepository
                .findByProcessDefinitionKey(processDefinitionKey)
                .orElseThrow(() -> new IllegalStateException(
                        "Process definition upsert did not return a row: " + processDefinitionKey
                ));
    }

    private ProcessInstanceEntity processInstance(
            Long processInstanceKey,
            ProcessDefinitionEntity processDefinition
    ) {
        processInstanceRepository.upsert(processInstanceKey, processDefinition.getId());
        return processInstanceRepository
                .findByProcessInstanceKey(processInstanceKey)
                .orElseThrow(() -> new IllegalStateException(
                        "Process instance upsert did not return a row: " + processInstanceKey
                ));
    }

    private BpmnElementEntity bpmnElement(ProcessDefinitionEntity processDefinition, String bpmnElementId) {
        bpmnElementRepository.insertIfMissing(processDefinition.getId(), bpmnElementId);
        return bpmnElementRepository
                .findByProcessDefinitionAndBpmnElementId(processDefinition, bpmnElementId)
                .orElseThrow(() -> new IllegalStateException(
                        "BPMN element upsert did not return a row: " + bpmnElementId
                ));
    }

    private KeiAnnotationEntity keiAnnotation(
            BpmnElementEntity bpmnElement,
            String keiId,
            String name,
            String unit,
            String targetValue
    ) {
        keiAnnotationRepository.insertIfMissing(bpmnElement.getId(), keiId);
        KeiAnnotationEntity entity = keiAnnotationRepository
                .findByBpmnElementAndKeiId(bpmnElement, keiId)
                .orElseThrow(() -> new IllegalStateException(
                        "KEI annotation upsert did not return a row: " + keiId
                ));

        entity.setName(name);
        entity.setUnit(unit);
        entity.setTargetValue(targetValue);
        return keiAnnotationRepository.save(entity);
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

    private Optional<KeiResultEntity> findResultForViolation(ThresholdViolation violation) {
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

        return keiResultRepository.findFirstByProcessInstanceAndBpmnElementAndKeiAnnotationOrderByEvaluatedAtDesc(
                processInstance,
                bpmnElement,
                keiAnnotation
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

    private void applyViolation(
            ThresholdViolationEntity entity,
            ThresholdViolation violation,
            KeiResultEntity result
    ) {
        entity.setEventId(violation.getEventId());
        entity.setKeiResult(result);
        entity.setStatus(violation.getStatus());
        entity.setCalculatedValue(violation.getCalculatedValue());
        entity.setTargetValue(violation.getTargetValue());
        entity.setDifference(violation.getDifference());
        entity.setOccurredAt(violation.getOccurredAt());
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
        entity.setCalculationRequestId(request.getCalculationRequestId());
        entity.setObservationId(request.getObservationId());
        entity.setSourceEventId(request.getSourceEventId());
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
        entity.setCalculationRequestId(request.getCalculationRequestId());
        entity.setObservationId(request.getObservationId());
        entity.setSourceEventId(request.getSourceEventId());
        entity.setCalculatedValue(request.getCalculatedValue());
        entity.setCalculatedUnit(request.getCalculatedUnit());
        entity.setCalculatedAt(request.getCalculatedAt());
        entity.setTargetValue(request.getTargetValue());
        entity.setDifference(request.getDifference());
        entity.setEvaluationStatus(request.getEvaluationStatus());
        entity.setEvaluatedAt(request.getOccurredAt());
        entity.setUpdatedAt(Instant.now());
    }

    private void applyProcessModel(ProcessDefinitionEntity entity, RegisterProcessModelRequest request) {
        entity.setDeploymentKey(request.getDeploymentKey());
        entity.setVersion(request.getVersion());
        entity.setDeployedAt(Instant.now());
    }

    private void applyProcessModelElement(BpmnElementEntity entity, ProcessModelElement element) {
        entity.setElementName(element.getName());
        entity.setElementType(element.getType());
    }

    private void applyProcessModelKeiAnnotation(
            KeiAnnotationEntity entity,
            ProcessModelKeiAnnotation annotation
    ) {
        entity.setIcon(annotation.getIcon());
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
