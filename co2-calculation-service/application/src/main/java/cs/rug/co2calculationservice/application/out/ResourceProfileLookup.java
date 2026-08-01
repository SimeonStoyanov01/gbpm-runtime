package cs.rug.co2calculationservice.application.out;

import cs.rug.co2calculationservice.application.model.ResourceProfile;

import java.util.Optional;

public interface ResourceProfileLookup {

    Optional<ResourceProfile> findResourceProfile(String referenceSetId, String resourceName);
}
