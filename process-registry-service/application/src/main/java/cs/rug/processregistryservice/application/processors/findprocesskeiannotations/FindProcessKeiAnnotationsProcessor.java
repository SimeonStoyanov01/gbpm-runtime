package cs.rug.processregistryservice.application.processors.findprocesskeiannotations;

import cs.rug.processregistryservice.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsOperation;
import cs.rug.processregistryservice.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsRequest;
import cs.rug.processregistryservice.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsResponse;
import cs.rug.processregistryservice.application.out.monitoring.MonitoringProcessModelClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindProcessKeiAnnotationsProcessor implements FindProcessKeiAnnotationsOperation {

    private final MonitoringProcessModelClient monitoringProcessModelClient;

    @Override
    public FindProcessKeiAnnotationsResponse process(FindProcessKeiAnnotationsRequest request) {
        return FindProcessKeiAnnotationsResponse
                .builder()
                .elementKeiAnnotations(monitoringProcessModelClient
                        .findProcessModel(request.getProcessDefinitionKey()))
                .build();
    }
}
