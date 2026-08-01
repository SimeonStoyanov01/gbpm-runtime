package cs.rug.co2calculationservice.infrastructure.persistence.repository;

import cs.rug.co2calculationservice.infrastructure.persistence.entity.EmissionFactorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmissionFactorRepository extends JpaRepository<EmissionFactorEntity, UUID> {

    Optional<EmissionFactorEntity> findByReferenceSetIdIgnoreCaseAndFuelTypeIgnoreCase(
            String referenceSetId,
            String fuelType
    );

    boolean existsByReferenceSetIdIgnoreCase(String referenceSetId);
}
