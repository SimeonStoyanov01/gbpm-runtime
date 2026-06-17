package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiAnnotationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KeiAnnotationJpaRepository extends JpaRepository<KeiAnnotationEntity, Long> {

    Optional<KeiAnnotationEntity> findByBpmnElementAndKeiId(BpmnElementEntity bpmnElement, String keiId);

    @Modifying
    @Query(
            value = """
                    INSERT INTO kei_annotation (bpmn_element_id, kei_id)
                    VALUES (:bpmnElementId, :keiId)
                    ON CONFLICT (bpmn_element_id, kei_id)
                    DO NOTHING
                    """,
            nativeQuery = true
    )
    void insertIfMissing(
            @Param("bpmnElementId") Long bpmnElementId,
            @Param("keiId") String keiId
    );
}
