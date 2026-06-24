package cs.rug.gbpmruntime.observation.application.out;

import cs.rug.gbpmruntime.observation.api.model.KeiAnnotation;

import java.util.List;

public interface KeiAnnotationLookupClient {

    List<KeiAnnotation> findKeiAnnotations(Long processDefinitionKey, String bpmnElementId);
}
