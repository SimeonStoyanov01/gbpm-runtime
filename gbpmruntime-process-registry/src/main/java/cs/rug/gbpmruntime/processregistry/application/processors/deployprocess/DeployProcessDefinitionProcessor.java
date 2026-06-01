package cs.rug.gbpmruntime.processregistry.application.processors.deployprocess;

import cs.rug.gbpmruntime.engineintegration.api.operations.deployprocess.DeployProcessToEngineOperation;
import cs.rug.gbpmruntime.engineintegration.api.operations.deployprocess.DeployProcessToEngineRequest;
import cs.rug.gbpmruntime.engineintegration.api.operations.deployprocess.DeployProcessToEngineResponse;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.gbpmruntime.processregistry.application.bpmn4es.Bpmn4esKeiAnnotationParser;
import cs.rug.gbpmruntime.processregistry.application.bpmn4es.ElementKeiAnnotations;
import cs.rug.gbpmruntime.processregistry.application.registry.ProcessKeiAnnotationRegistry;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployProcessDefinitionProcessor implements DeployProcessDefinitionOperation {

    private final DeployProcessToEngineOperation deployProcessToEngineOperation;
    private final Bpmn4esKeiAnnotationParser bpmn4esKeiAnnotationParser;
    private final ProcessKeiAnnotationRegistry processKeiAnnotationRegistry;

    @Override
    public DeployProcessDefinitionResponse process(DeployProcessDefinitionRequest request) {
        DeployProcessToEngineRequest deployProcessToEngineRequest = DeployProcessToEngineRequest
                .builder()
                .resourceName(request.getResourceName())
                .resourceContent(request.getBpmnXml())
                .targetEngine(request.getTargetEngine())
                .build();


        DeployProcessToEngineResponse deployProcessToEngineResponse = deployProcessToEngineOperation
                .process(deployProcessToEngineRequest);

        List<ElementKeiAnnotations> elementKeiAnnotations = bpmn4esKeiAnnotationParser
                .parse(request.getBpmnXml());

        processKeiAnnotationRegistry.registerProcessAnnotations(
                Long.valueOf(deployProcessToEngineResponse.getProcessDefinitionKey()),
                elementKeiAnnotations
        );

        return DeployProcessDefinitionResponse
                .builder()
                .deploymentKey(deployProcessToEngineResponse.getDeploymentKey())
                .processDefinitionKey(deployProcessToEngineResponse.getProcessDefinitionKey())
                .bpmnProcessId(deployProcessToEngineResponse.getBpmnProcessId())
                .version(deployProcessToEngineResponse.getVersion())
                .resourceName(deployProcessToEngineResponse.getResourceName())
                .tenantId(deployProcessToEngineResponse.getTenantId())
                .status("DEPLOYED")
                .build();
    }
}
