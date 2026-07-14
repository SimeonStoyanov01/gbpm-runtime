package cs.rug.observationservice.infrastructure.client.processregistry;

import cs.rug.observationservice.api.model.KeiAnnotation;
import cs.rug.observationservice.application.out.KeiAnnotationLookupClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessRegistryKeiAnnotationLookupClient implements KeiAnnotationLookupClient {

    private final ProcessRegistryFeignClient processRegistryFeignClient;

    @Override
    public List<KeiAnnotation> findKeiAnnotations(Long processDefinitionKey, String bpmnElementId) {
        List<KeiAnnotation> annotations = processRegistryFeignClient
                .findActivityKeiAnnotations(processDefinitionKey, bpmnElementId)
                .getKeiAnnotations();

        if (annotations == null) {
            return List.of();
        }

        return annotations;
    }
}
