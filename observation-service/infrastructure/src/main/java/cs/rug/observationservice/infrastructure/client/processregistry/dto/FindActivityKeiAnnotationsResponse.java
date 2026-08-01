package cs.rug.observationservice.infrastructure.client.processregistry.dto;

import cs.rug.observationservice.api.model.KeiAnnotation;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FindActivityKeiAnnotationsResponse {
    private List<KeiAnnotation> keiAnnotations;
}
