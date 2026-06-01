package cs.rug.gbpmruntime.processregistry.application.registry;

import cs.rug.gbpmruntime.processregistry.application.bpmn4es.KeiMetadata;
import cs.rug.gbpmruntime.processregistry.application.bpmn4es.ElementKeiAnnotations;

import java.util.List;

public interface ProcessKeiAnnotationRegistry {

    void registerProcessAnnotations(Long processDefinitionKey, List<ElementKeiAnnotations> elementKeiAnnotations);

    List<ElementKeiAnnotations> findKeiAnnotationsByProcessDefinitionKey(Long processDefinitionKey);

    List<KeiMetadata> findKeiMetadataByProcessDefinitionKeyAndBpmnElementId(Long processDefinitionKey, String bpmnElementId);
}
