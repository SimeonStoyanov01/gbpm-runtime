package cs.rug.co2calculationservice.infrastructure.persistence;

import cs.rug.co2calculationservice.application.model.ResourceProfile;
import cs.rug.co2calculationservice.application.out.ResourceProfileLookup;
import cs.rug.co2calculationservice.infrastructure.persistence.entity.ResourceProfileEntity;
import cs.rug.co2calculationservice.infrastructure.persistence.repository.ResourceProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaResourceProfileLookup implements ResourceProfileLookup {

    private final ResourceProfileRepository resourceProfileRepository;

    @Override
    public Optional<ResourceProfile> findResourceProfile(String referenceSetId, String resourceName) {
        return resourceProfileRepository
                .findByReferenceSetIdIgnoreCaseAndNameIgnoreCase(referenceSetId, resourceName)
                .map(this::toModel);
    }

    private ResourceProfile toModel(ResourceProfileEntity entity) {
        return ResourceProfile
                .builder()
                .name(entity.getName())
                .type(entity.getType())
                .fuelPerUse(entity.getFuelPerUse())
                .fuelType(entity.getFuelType())
                .fuelUnit(entity.getFuelUnit())
                .timeUnit(entity.getTimeUnit())
                .build();
    }
}
