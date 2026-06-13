package cs.rug.gbpmruntime.processregistry.infrastructure.memory;

import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.KeiMetadata;
import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.ElementKeiAnnotations;
import cs.rug.gbpmruntime.processregistry.application.out.keiregistry.ProcessKeiAnnotationRegistry;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryProcessKeiAnnotationRegistry implements ProcessKeiAnnotationRegistry {

    private final Map<Long, Map<String, List<KeiMetadata>>> mappingsByProcessDefinitionKey = new ConcurrentHashMap<>();

    @Override
    public void registerProcessAnnotations(Long processDefinitionKey, List<ElementKeiAnnotations> elementKeiAnnotations) {
        Map<String, List<KeiMetadata>> keiMetadataByElementId = new LinkedHashMap<>();
        for (ElementKeiAnnotations elementKeiAnnotation : elementKeiAnnotations) {
            keiMetadataByElementId.put(elementKeiAnnotation.getBpmnElementId(), List.copyOf(elementKeiAnnotation.getKeiMetadata()));
        }
        mappingsByProcessDefinitionKey.put(processDefinitionKey, Collections.unmodifiableMap(keiMetadataByElementId));
    }

    @Override
    public List<ElementKeiAnnotations> findKeiAnnotationsByProcessDefinitionKey(Long processDefinitionKey) {
        return mappingsByProcessDefinitionKey
                .getOrDefault(processDefinitionKey, Map.of())
                .entrySet()
                .stream()
                .map(entry -> ElementKeiAnnotations
                        .builder()
                        .bpmnElementId(entry.getKey())
                        .keiMetadata(entry.getValue())
                        .build())
                .toList();
    }

    @Override
    public List<KeiMetadata> findKeiMetadataByProcessDefinitionKeyAndBpmnElementId(Long processDefinitionKey, String bpmnElementId) {

        return mappingsByProcessDefinitionKey
                .getOrDefault(processDefinitionKey, Map.of())
                .getOrDefault(bpmnElementId, List.of());
    }
}
