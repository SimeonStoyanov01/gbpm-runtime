package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProcessInstanceJpaRepository extends JpaRepository<ProcessInstanceEntity, UUID> {

    Optional<ProcessInstanceEntity> findByProcessInstanceKey(Long processInstanceKey);

    @Modifying
    @Query(
            value = """
                    INSERT INTO process_instance (id, process_instance_key, process_definition_id)
                    VALUES (:id, :processInstanceKey, :processDefinitionId)
                    ON CONFLICT (process_instance_key)
                    DO UPDATE SET process_definition_id = EXCLUDED.process_definition_id
                    """,
            nativeQuery = true
    )
    void saveProcessInstance(
            @Param("id") UUID id,
            @Param("processInstanceKey") Long processInstanceKey,
            @Param("processDefinitionId") UUID processDefinitionId
    );
}
