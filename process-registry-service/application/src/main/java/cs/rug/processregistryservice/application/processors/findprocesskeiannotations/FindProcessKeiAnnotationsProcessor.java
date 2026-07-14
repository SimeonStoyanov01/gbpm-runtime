package cs.rug.processregistryservice.application.processors.findprocesskeiannotations;

import cs.rug.processregistryservice.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsOperation;
import cs.rug.processregistryservice.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsRequest;
import cs.rug.processregistryservice.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsResponse;
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
                .elementKeiAnnotations(processKeiAnnotationRegistry
                        .findKeiAnnotationsByProcessDefinitionKey(request.getProcessDefinitionKey()))
                .build();
    }
}
