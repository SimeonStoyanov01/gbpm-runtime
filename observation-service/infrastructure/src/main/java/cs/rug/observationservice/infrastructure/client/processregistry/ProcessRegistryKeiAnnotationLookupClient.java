package cs.rug.observationservice.infrastructure.client.processregistry;

import cs.rug.observationservice.api.model.KeiAnnotation;
import cs.rug.observationservice.application.out.KeiAnnotationLookupClient;
import cs.rug.observationservice.infrastructure.client.processregistry.dto.ProcessRegistryKeiAnnotationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessRegistryKeiAnnotationLookupClient implements KeiAnnotationLookupClient {

    private final ProcessRegistryFeignClient processRegistryFeignClient;

    @Override
    public List<KeiAnnotation> findKeiAnnotations(Long processDefinitionKey, String bpmnElementId) {
        List<ProcessRegistryKeiAnnotationDto> annotations = processRegistryFeignClient
                .findActivityKeiAnnotations(processDefinitionKey, bpmnElementId)
                .getBpmn4esKeiAnnotations();

        if (annotations == null) {
            return List.of();
        }

        return annotations
                .stream()
                .map(this::toKeiAnnotation)
                .toList();
    }

    private KeiAnnotation toKeiAnnotation(ProcessRegistryKeiAnnotationDto annotation) {
        return KeiAnnotation
                .builder()
                .id(annotation.getId())
                .unit(annotation.getUnit())
                .targetValue(annotation.getTargetValue())
                .icon(annotation.getIcon())
                .build();
    }
}
