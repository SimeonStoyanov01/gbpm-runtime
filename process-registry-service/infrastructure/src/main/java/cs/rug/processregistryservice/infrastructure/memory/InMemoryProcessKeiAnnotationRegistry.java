package cs.rug.processregistryservice.infrastructure.memory;

import cs.rug.processregistryservice.api.model.ElementKeiAnnotations;
import cs.rug.processregistryservice.api.model.KeiAnnotation;
import cs.rug.processregistryservice.application.out.keiregistry.ProcessKeiAnnotationRegistry;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryProcessKeiAnnotationRegistry implements ProcessKeiAnnotationRegistry {

    private final Map<Long, List<ElementKeiAnnotations>> mappingsByProcessDefinitionKey = new ConcurrentHashMap<>();

    @Override
    public void registerProcessAnnotations(Long processDefinitionKey, List<ElementKeiAnnotations> elementKeiAnnotations) {
        mappingsByProcessDefinitionKey.put(processDefinitionKey, List.copyOf(elementKeiAnnotations));
    }

    @Override
    public List<ElementKeiAnnotations> findKeiAnnotationsByProcessDefinitionKey(Long processDefinitionKey) {
        return mappingsByProcessDefinitionKey
                .getOrDefault(processDefinitionKey, List.of());
    }

    @Override
    public List<KeiAnnotation> findKeiAnnotationsByProcessDefinitionKeyAndBpmnElementId(
            Long processDefinitionKey,
            String bpmnElementId
    ) {
        return mappingsByProcessDefinitionKey
                .getOrDefault(processDefinitionKey, List.of())
                .stream()
                .filter(element -> bpmnElementId.equals(element.getBpmnElementId()))
                .findFirst()
                .map(ElementKeiAnnotations::getKeiAnnotations)
                .orElseGet(List::of);
    }
}
