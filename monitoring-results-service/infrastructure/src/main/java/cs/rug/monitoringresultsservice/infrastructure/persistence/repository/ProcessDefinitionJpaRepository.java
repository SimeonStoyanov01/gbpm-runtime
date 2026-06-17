package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessDefinitionJpaRepository extends JpaRepository<ProcessDefinitionEntity, Long> {

    Optional<ProcessDefinitionEntity> findByProcessDefinitionKey(Long processDefinitionKey);
}
