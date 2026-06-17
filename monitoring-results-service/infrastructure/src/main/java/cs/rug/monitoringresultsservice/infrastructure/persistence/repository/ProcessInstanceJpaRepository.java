package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessInstanceJpaRepository extends JpaRepository<ProcessInstanceEntity, Long> {

    Optional<ProcessInstanceEntity> findByProcessInstanceKey(Long processInstanceKey);
}
