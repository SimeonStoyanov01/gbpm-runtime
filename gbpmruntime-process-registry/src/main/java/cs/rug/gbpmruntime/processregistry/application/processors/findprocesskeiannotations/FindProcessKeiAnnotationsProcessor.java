package cs.rug.gbpmruntime.processregistry.application.processors.findprocesskeiannotations;

import cs.rug.gbpmruntime.processregistry.api.model.ElementKeiAnnotationsModel;
import cs.rug.gbpmruntime.processregistry.api.model.KeiAnnotationModel;
import cs.rug.gbpmruntime.processregistry.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsResponse;
import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.ElementKeiAnnotations;
import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.KeiMetadata;
import cs.rug.gbpmruntime.processregistry.application.out.keiregistry.ProcessKeiAnnotationRegistry;
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
