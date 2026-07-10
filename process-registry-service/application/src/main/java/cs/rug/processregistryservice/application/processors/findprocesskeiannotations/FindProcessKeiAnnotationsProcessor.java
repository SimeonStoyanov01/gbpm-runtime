package cs.rug.processregistryservice.application.processors.findprocesskeiannotations;

import cs.rug.processregistryservice.api.model.ElementKeiAnnotationsModel;
import cs.rug.processregistryservice.api.model.KeiAnnotationModel;
import cs.rug.processregistryservice.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsOperation;
import cs.rug.processregistryservice.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsRequest;
import cs.rug.processregistryservice.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsResponse;
import cs.rug.processregistryservice.application.model.bpmn4es.ElementKeiAnnotations;
import cs.rug.processregistryservice.application.model.bpmn4es.KeiMetadata;
import cs.rug.processregistryservice.application.out.keiregistry.ProcessKeiAnnotationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindProcessKeiAnnotationsProcessor implements FindProcessKeiAnnotationsOperation {

    private final ProcessKeiAnnotationRegistry processKeiAnnotationRegistry;

    @Override
    public FindProcessKeiAnnotationsResponse process(FindProcessKeiAnnotationsRequest request) {
        return FindProcessKeiAnnotationsResponse
                .builder()
                .bpmn4esElementKeiAnnotations(processKeiAnnotationRegistry
                        .findKeiAnnotationsByProcessDefinitionKey(request.getProcessDefinitionKey())
                        .stream()
                        .map(this::toModel)
                        .toList())
                .build();
    }

    private ElementKeiAnnotationsModel toModel(ElementKeiAnnotations elementKeiAnnotations) {
        return ElementKeiAnnotationsModel
                .builder()
                .bpmnElementId(elementKeiAnnotations.getBpmnElementId())
                .bpmn4esKeiAnnotations(elementKeiAnnotations.getKeiMetadata()
                        .stream()
                        .map(this::toModel)
                        .toList())
                .build();
    }

    private KeiAnnotationModel toModel(KeiMetadata keiMetadata) {
        return KeiAnnotationModel
                .builder()
                .id(keiMetadata.getId())
                .unit(keiMetadata.getUnit())
                .targetValue(keiMetadata.getTargetValue())
                .icon(keiMetadata.getIcon())
                .build();
    }
}
