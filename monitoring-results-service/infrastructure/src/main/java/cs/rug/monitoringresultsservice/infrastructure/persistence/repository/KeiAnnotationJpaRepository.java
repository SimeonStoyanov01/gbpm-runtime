package cs.rug.monitoringresultsservice.infrastructure.persistence.repository;

import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiAnnotationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface KeiAnnotationJpaRepository extends JpaRepository<KeiAnnotationEntity, UUID> {

    Optional<KeiAnnotationEntity> findByBpmnElementAndKeiId(BpmnElementEntity bpmnElement, String keiId);

    @Modifying
    @Query(
            value = """
                    INSERT INTO kei_annotation (id, bpmn_element_id, kei_id)
                    VALUES (:id, :bpmnElementId, :keiId)
                    ON CONFLICT (bpmn_element_id, kei_id)
                    DO NOTHING
                    """,
            nativeQuery = true
    )
    void insertIfMissing(
            @Param("id") UUID id,
            @Param("bpmnElementId") UUID bpmnElementId,
            @Param("keiId") String keiId
    );
}
