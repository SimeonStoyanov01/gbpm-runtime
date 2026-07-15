package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiAnnotationEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiResultEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
            SELECT result
            FROM KeiResultEntity result
            JOIN result.processInstance processInstance
            JOIN processInstance.processDefinition processDefinition
            WHERE (:processInstanceKey IS NULL OR processInstance.processInstanceKey = :processInstanceKey)
              AND (:processDefinitionKey IS NULL OR processDefinition.processDefinitionKey = :processDefinitionKey)
              AND (:bpmnProcessId IS NULL OR processDefinition.bpmnProcessId = :bpmnProcessId)
              AND (:evaluationStatus IS NULL OR result.evaluationStatus = :evaluationStatus)
            """)
    List<KeiResultEntity> findAllByFilters(
            @Param("processInstanceKey") Long processInstanceKey,
            @Param("processDefinitionKey") Long processDefinitionKey,
            @Param("bpmnProcessId") String bpmnProcessId,
            @Param("evaluationStatus") String evaluationStatus
    );
}
