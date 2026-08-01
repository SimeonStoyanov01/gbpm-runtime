package cs.rug.observationservice.application.out;

import cs.rug.observationservice.api.model.KeiAnnotation;

import java.util.List;

public interface KeiAnnotationLookupClient {

    List<KeiAnnotation> findKeiAnnotations(Long processDefinitionKey, String bpmnElementId);
}
