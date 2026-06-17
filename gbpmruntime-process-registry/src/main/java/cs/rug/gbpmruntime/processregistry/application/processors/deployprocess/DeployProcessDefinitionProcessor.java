package cs.rug.gbpmruntime.processregistry.application.processors.deployprocess;

import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.gbpmruntime.processregistry.application.out.bpmn4es.Bpmn4esKeiAnnotationParser;
import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.ElementKeiAnnotations;
import cs.rug.gbpmruntime.processregistry.application.out.engine.WorkflowEngineCommandClient;
import cs.rug.gbpmruntime.processregistry.application.out.keiregistry.ProcessKeiAnnotationRegistry;
import cs.rug.gbpmruntime.processregistry.application.out.monitoring.MonitoringProcessModelClient;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployProcessDefinitionProcessor implements DeployProcessDefinitionOperation {

    private final WorkflowEngineCommandClient workflowEngineCommandClient;
    private final Bpmn4esKeiAnnotationParser bpmn4esKeiAnnotationParser;
    private final ProcessKeiAnnotationRegistry processKeiAnnotationRegistry;
    private final MonitoringProcessModelClient monitoringProcessModelClient;

    @Override
    public DeployProcessDefinitionResponse process(DeployProcessDefinitionRequest request) {
        DeployProcessDefinitionResponse deployProcessDefinitionResponse = workflowEngineCommandClient
                .deployProcess(request);

        List<ElementKeiAnnotations> elementKeiAnnotations = bpmn4esKeiAnnotationParser
                .parse(request.getBpmnXml());

        processKeiAnnotationRegistry.registerProcessAnnotations(
                Long.valueOf(deployProcessDefinitionResponse.getProcessDefinitionKey()),
                elementKeiAnnotations
        );
        monitoringProcessModelClient.registerProcessModel(deployProcessDefinitionResponse, elementKeiAnnotations);

        return deployProcessDefinitionResponse;
    }
}
