package cs.rug.gbpmruntime.processregistry.application.out.keiregistry;

import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.KeiMetadata;
import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.ElementKeiAnnotations;

import java.util.List;

public interface ProcessKeiAnnotationRegistry {

    void registerProcessAnnotations(Long processDefinitionKey, List<ElementKeiAnnotations> elementKeiAnnotations);

    List<ElementKeiAnnotations> findKeiAnnotationsByProcessDefinitionKey(Long processDefinitionKey);

    List<KeiMetadata> findKeiMetadataByProcessDefinitionKeyAndBpmnElementId(Long processDefinitionKey, String bpmnElementId);
}
