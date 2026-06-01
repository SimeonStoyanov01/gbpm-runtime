package cs.rug.gbpmruntime.processregistry.application.processors.findactivitykeiannotations;

import cs.rug.gbpmruntime.processregistry.api.model.KeiAnnotationModel;
import cs.rug.gbpmruntime.processregistry.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsResponse;
import cs.rug.gbpmruntime.processregistry.application.bpmn4es.KeiMetadata;
import cs.rug.gbpmruntime.processregistry.application.registry.ProcessKeiAnnotationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindActivityKeiAnnotationsProcessor implements FindActivityKeiAnnotationsOperation {

    private final ProcessKeiAnnotationRegistry processAnnotationRegistry;

    @Override
    public FindActivityKeiAnnotationsResponse process(FindActivityKeiAnnotationsRequest request) {
        return FindActivityKeiAnnotationsResponse
                .builder()
                .bpmn4esKeiAnnotations(processAnnotationRegistry.findKeiMetadataByProcessDefinitionKeyAndBpmnElementId(
                        request.getProcessDefinitionKey(),
                        request.getBpmnElementId()
                ).stream().map(this::toModel).toList())
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
