package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BpmnElementJpaRepository extends JpaRepository<BpmnElementEntity, Long> {

    Optional<BpmnElementEntity> findByProcessDefinitionAndBpmnElementId(
            ProcessDefinitionEntity processDefinition,
            String bpmnElementId
    );

    @Modifying
    @Query(
            value = """
                    INSERT INTO bpmn_element (process_definition_id, bpmn_element_id)
                    VALUES (:processDefinitionId, :bpmnElementId)
                    ON CONFLICT (process_definition_id, bpmn_element_id)
                    DO NOTHING
                    """,
            nativeQuery = true
    )
    void insertIfMissing(
            @Param("processDefinitionId") Long processDefinitionId,
            @Param("bpmnElementId") String bpmnElementId
    );
}
