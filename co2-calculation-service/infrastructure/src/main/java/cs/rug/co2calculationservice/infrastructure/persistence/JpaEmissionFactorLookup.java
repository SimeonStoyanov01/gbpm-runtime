package cs.rug.co2calculationservice.infrastructure.persistence;

import cs.rug.co2calculationservice.application.model.EmissionFactor;
import cs.rug.co2calculationservice.application.out.EmissionFactorLookup;
import cs.rug.co2calculationservice.infrastructure.persistence.entity.EmissionFactorEntity;
import cs.rug.co2calculationservice.infrastructure.persistence.repository.EmissionFactorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaEmissionFactorLookup implements EmissionFactorLookup {

    private final EmissionFactorRepository emissionFactorRepository;

    @Override
    public Optional<EmissionFactor> findEmissionFactor(String referenceSetId, String fuelType) {
        return emissionFactorRepository
                .findByReferenceSetIdIgnoreCaseAndFuelTypeIgnoreCase(referenceSetId, fuelType)
                .map(this::toModel);
    }

    private EmissionFactor toModel(EmissionFactorEntity entity) {
        return EmissionFactor
                .builder()
                .fuelType(entity.getFuelType())
                .unit(entity.getUnit())
                .factor(entity.getFactor())
                .build();
    }
}
