package cs.rug.processregistryservice.application.out.keiregistry;

import cs.rug.processregistryservice.api.model.ElementKeiAnnotations;
import cs.rug.processregistryservice.api.model.KeiAnnotation;

import java.util.List;

public interface ProcessKeiAnnotationRegistry {

    void registerProcessAnnotations(Long processDefinitionKey, List<ElementKeiAnnotations> elementKeiAnnotations);

    List<ElementKeiAnnotations> findKeiAnnotationsByProcessDefinitionKey(Long processDefinitionKey);

    List<KeiAnnotation> findKeiAnnotationsByProcessDefinitionKeyAndBpmnElementId(
            Long processDefinitionKey,
            String bpmnElementId
    );
}
