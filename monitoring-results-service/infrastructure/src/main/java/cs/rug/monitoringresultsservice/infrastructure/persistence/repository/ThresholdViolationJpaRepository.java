package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ThresholdViolationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThresholdViolationJpaRepository extends JpaRepository<ThresholdViolationEntity, Long> {

    Optional<ThresholdViolationEntity> findByEventId(String eventId);

    List<ThresholdViolationEntity> findByKeiResultProcessInstanceProcessDefinitionProcessDefinitionKey(
            Long processDefinitionKey
    );

    List<ThresholdViolationEntity> findByKeiResultProcessInstanceProcessDefinitionBpmnProcessId(String bpmnProcessId);
}
