package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiAnnotationEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiResultEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KeiResultJpaRepository extends JpaRepository<KeiResultEntity, UUID> {

    Optional<KeiResultEntity> findByCalculationEventId(String calculationEventId);

    Optional<KeiResultEntity> findByEvaluationEventId(String evaluationEventId);

    Optional<KeiResultEntity> findByProcessInstanceAndBpmnElementAndKeiAnnotationAndElementInstanceKey(
            ProcessInstanceEntity processInstance,
            BpmnElementEntity bpmnElement,
            KeiAnnotationEntity keiAnnotation,
            Long elementInstanceKey
    );

    List<KeiResultEntity> findByProcessInstanceProcessInstanceKey(Long processInstanceKey);

    List<KeiResultEntity> findByProcessInstanceProcessDefinitionProcessDefinitionKey(Long processDefinitionKey);

    List<KeiResultEntity> findByProcessInstanceProcessDefinitionBpmnProcessId(String bpmnProcessId);

    List<KeiResultEntity> findByEvaluationStatus(String evaluationStatus);
}
