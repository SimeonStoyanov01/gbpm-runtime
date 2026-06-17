package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiAnnotationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeiAnnotationJpaRepository extends JpaRepository<KeiAnnotationEntity, Long> {

    Optional<KeiAnnotationEntity> findByBpmnElementAndKeiId(BpmnElementEntity bpmnElement, String keiId);
}
