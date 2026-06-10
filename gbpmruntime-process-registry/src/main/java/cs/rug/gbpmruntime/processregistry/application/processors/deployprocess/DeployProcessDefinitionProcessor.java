package cs.rug.gbpmruntime.processregistry.application.processors.deployprocess;

import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.gbpmruntime.processregistry.application.bpmn4es.Bpmn4esKeiAnnotationParser;
import cs.rug.gbpmruntime.processregistry.application.bpmn4es.ElementKeiAnnotations;
import cs.rug.gbpmruntime.processregistry.application.registry.ProcessKeiAnnotationRegistry;
import cs.rug.gbpmruntime.processregistry.infrastructure.clients.Camunda8IntegrationClient;
import cs.rug.gbpmruntime.processregistry.infrastructure.clients.dto.deployprocess.DeployProcessToEngineRequest;
import cs.rug.gbpmruntime.processregistry.infrastructure.clients.dto.deployprocess.DeployProcessToEngineResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployProcessDefinitionProcessor implements DeployProcessDefinitionOperation {

    private final Camunda8IntegrationClient camunda8IntegrationClient;
    private final Bpmn4esKeiAnnotationParser bpmn4esKeiAnnotationParser;
    private final ProcessKeiAnnotationRegistry processKeiAnnotationRegistry;

    @Override
    public DeployProcessDefinitionResponse process(DeployProcessDefinitionRequest request) {
        DeployProcessToEngineRequest deployProcessToEngineRequest = DeployProcessToEngineRequest
                .builder()
                .resourceName(request.getResourceName())
                .resourceContent(request.getBpmnXml())
                .build();

        DeployProcessToEngineResponse deployProcessToEngineResponse = camunda8IntegrationClient
                .deployProcessDefinition(deployProcessToEngineRequest);

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
