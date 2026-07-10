package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProcessDefinitionJpaRepository extends JpaRepository<ProcessDefinitionEntity, UUID> {

    Optional<ProcessDefinitionEntity> findByProcessDefinitionKey(Long processDefinitionKey);

    @Modifying
    @Query(
            value = """
                    INSERT INTO process_definition (
                        id,
                        process_definition_key,
                        bpmn_process_id,
                        deployment_key,
                        version,
                        bpmn_xml,
                        deployed_at
                    )
                    VALUES (
                        :id,
                        :processDefinitionKey,
                        :bpmnProcessId,
                        :deploymentKey,
                        :version,
                        :bpmnXml,
                        CASE WHEN :deploymentKey IS NULL THEN NULL ELSE CURRENT_TIMESTAMP END
                    )
                    ON CONFLICT (process_definition_key)
                    DO UPDATE SET
                        bpmn_process_id = EXCLUDED.bpmn_process_id,
                        deployment_key = COALESCE(EXCLUDED.deployment_key, process_definition.deployment_key),
                        version = COALESCE(EXCLUDED.version, process_definition.version),
                        bpmn_xml = COALESCE(EXCLUDED.bpmn_xml, process_definition.bpmn_xml),
                        deployed_at = COALESCE(process_definition.deployed_at, EXCLUDED.deployed_at)
                    """,
            nativeQuery = true
    )
    void saveRegisteredProcessDefinition(
            @Param("id") UUID id,
            @Param("processDefinitionKey") Long processDefinitionKey,
            @Param("bpmnProcessId") String bpmnProcessId,
            @Param("deploymentKey") Long deploymentKey,
            @Param("version") Integer version,
            @Param("bpmnXml") String bpmnXml
    );
}
