package cs.rug.processregistryservice.application.processors.findactivitykeiannotations;

import cs.rug.processregistryservice.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsOperation;
import cs.rug.processregistryservice.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsRequest;
import cs.rug.processregistryservice.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsResponse;
import cs.rug.processregistryservice.api.model.ElementKeiAnnotations;
import cs.rug.processregistryservice.application.out.monitoring.MonitoringProcessModelClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindActivityKeiAnnotationsProcessor implements FindActivityKeiAnnotationsOperation {

    private final MonitoringProcessModelClient monitoringProcessModelClient;

    @Override
    public FindActivityKeiAnnotationsResponse process(FindActivityKeiAnnotationsRequest request) {
        return FindActivityKeiAnnotationsResponse
                .builder()
                .keiAnnotations(monitoringProcessModelClient
                        .findProcessModel(request.getProcessDefinitionKey())
                        .stream()
                        .filter(element -> request.getBpmnElementId().equals(element.getBpmnElementId()))
                        .findFirst()
                        .map(ElementKeiAnnotations::getKeiAnnotations)
                        .orElseGet(List::of))
                .build();
    }
}
