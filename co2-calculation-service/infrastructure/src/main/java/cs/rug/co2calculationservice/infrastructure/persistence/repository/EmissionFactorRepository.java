package cs.rug.co2calculationservice.infrastructure.persistence.repository;

import cs.rug.co2calculationservice.infrastructure.persistence.entity.EmissionFactorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmissionFactorRepository extends JpaRepository<EmissionFactorEntity, Long> {

    Optional<EmissionFactorEntity> findByReferenceSetIdIgnoreCaseAndFuelTypeIgnoreCase(
            String referenceSetId,
            String fuelType
    );

    boolean existsByReferenceSetIdIgnoreCase(String referenceSetId);
}
