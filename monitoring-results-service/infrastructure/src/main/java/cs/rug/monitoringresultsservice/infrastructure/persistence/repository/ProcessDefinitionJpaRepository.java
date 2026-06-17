package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProcessDefinitionJpaRepository extends JpaRepository<ProcessDefinitionEntity, Long> {

    Optional<ProcessDefinitionEntity> findByProcessDefinitionKey(Long processDefinitionKey);

    @Modifying
    @Query(
            value = """
                    INSERT INTO process_definition (process_definition_key, bpmn_process_id)
                    VALUES (:processDefinitionKey, :bpmnProcessId)
                    ON CONFLICT (process_definition_key)
                    DO UPDATE SET bpmn_process_id = EXCLUDED.bpmn_process_id
                    """,
            nativeQuery = true
    )
    void upsert(
            @Param("processDefinitionKey") Long processDefinitionKey,
            @Param("bpmnProcessId") String bpmnProcessId
    );
}
