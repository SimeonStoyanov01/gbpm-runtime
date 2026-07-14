package cs.rug.processregistryservice.application.processors.deployprocess;

import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionOperation;
import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionRequest;
import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.processregistryservice.api.model.ElementKeiAnnotations;
import cs.rug.processregistryservice.application.out.bpmn4es.Bpmn4esKeiAnnotationParser;
import cs.rug.processregistryservice.application.out.engine.WorkflowEngineCommandClient;
import cs.rug.processregistryservice.application.out.monitoring.MonitoringProcessModelClient;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployProcessDefinitionProcessor implements DeployProcessDefinitionOperation {

    private final WorkflowEngineCommandClient workflowEngineCommandClient;
    private final Bpmn4esKeiAnnotationParser bpmn4esKeiAnnotationParser;
    private final MonitoringProcessModelClient monitoringProcessModelClient;

    @Override
    public DeployProcessDefinitionResponse process(DeployProcessDefinitionRequest request) {
        DeployProcessDefinitionResponse deployProcessDefinitionResponse = workflowEngineCommandClient
                .deployProcess(request);

        List<ElementKeiAnnotations> elementKeiAnnotations = bpmn4esKeiAnnotationParser
                .parse(request.getBpmnXml());

        monitoringProcessModelClient.registerProcessModel(
                deployProcessDefinitionResponse,
                request.getBpmnXml(),
                elementKeiAnnotations
        );

        return deployProcessDefinitionResponse;
    }
}
