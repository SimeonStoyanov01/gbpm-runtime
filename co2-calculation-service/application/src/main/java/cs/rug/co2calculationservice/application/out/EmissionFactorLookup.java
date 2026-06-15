package cs.rug.co2calculationservice.application.out;

import cs.rug.co2calculationservice.application.model.EmissionFactor;

import java.util.Optional;

public interface EmissionFactorLookup {

    Optional<EmissionFactor> findEmissionFactor(String referenceSetId, String fuelType);
}
