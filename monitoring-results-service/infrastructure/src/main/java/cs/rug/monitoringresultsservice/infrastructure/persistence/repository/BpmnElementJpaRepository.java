package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BpmnElementJpaRepository extends JpaRepository<BpmnElementEntity, Long> {

    Optional<BpmnElementEntity> findByProcessDefinitionAndBpmnElementId(
            ProcessDefinitionEntity processDefinition,
            String bpmnElementId
    );
}
