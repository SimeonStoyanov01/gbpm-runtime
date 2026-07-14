package cs.rug.processregistryservice.application.processors.findactivitykeiannotations;

import cs.rug.processregistryservice.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsOperation;
import cs.rug.processregistryservice.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsRequest;
import cs.rug.processregistryservice.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsResponse;
import cs.rug.processregistryservice.application.out.keiregistry.ProcessKeiAnnotationRegistry;
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
                .keiAnnotations(processAnnotationRegistry.findKeiAnnotationsByProcessDefinitionKeyAndBpmnElementId(
                        request.getProcessDefinitionKey(),
                        request.getBpmnElementId()
                ))
                .build();
    }
}
