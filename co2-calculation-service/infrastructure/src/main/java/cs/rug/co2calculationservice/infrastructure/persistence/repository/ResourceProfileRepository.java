package cs.rug.co2calculationservice.infrastructure.persistence.repository;

import cs.rug.co2calculationservice.infrastructure.persistence.entity.ResourceProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResourceProfileRepository extends JpaRepository<ResourceProfileEntity, Long> {

    Optional<ResourceProfileEntity> findByReferenceSetIdIgnoreCaseAndNameIgnoreCase(
            String referenceSetId,
            String name
    );

    boolean existsByReferenceSetIdIgnoreCase(String referenceSetId);
}
