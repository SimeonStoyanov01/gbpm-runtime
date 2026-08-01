package cs.rug.processregistryservice.infrastructure.client.camunda8;

import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionRequest;
import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.processregistryservice.api.operations.startprocessinstance.StartProcessInstanceRequest;
import cs.rug.processregistryservice.api.operations.startprocessinstance.StartProcessInstanceResponse;
import cs.rug.processregistryservice.application.out.engine.WorkflowEngineCommandClient;
import cs.rug.processregistryservice.infrastructure.client.camunda8.dto.deployprocess.DeployProcessToEngineRequest;
import cs.rug.processregistryservice.infrastructure.client.camunda8.dto.deployprocess.DeployProcessToEngineResponse;
import cs.rug.processregistryservice.infrastructure.client.camunda8.dto.startprocess.StartProcessInEngineRequest;
import cs.rug.processregistryservice.infrastructure.client.camunda8.dto.startprocess.StartProcessInEngineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Camunda8WorkflowEngineCommandClientAdapter implements WorkflowEngineCommandClient {

    private final Camunda8IntegrationFeignClient camunda8IntegrationFeignClient;

    @Override
    public DeployProcessDefinitionResponse deployProcess(DeployProcessDefinitionRequest request) {
        DeployProcessToEngineResponse response = camunda8IntegrationFeignClient.deployProcessDefinition(
                DeployProcessToEngineRequest
                        .builder()
                        .resourceName(request.getResourceName())
                        .resourceContent(request.getBpmnXml())
                        .build()
        );

        return DeployProcessDefinitionResponse
                .builder()
                .deploymentKey(response.getDeploymentKey())
                .processDefinitionKey(response.getProcessDefinitionKey())
                .bpmnProcessId(response.getBpmnProcessId())
                .version(response.getVersion())
                .resourceName(response.getResourceName())
                .tenantId(response.getTenantId())
                .status("DEPLOYED")
                .build();
    }

    @Override
    public StartProcessInstanceResponse startProcessInstance(StartProcessInstanceRequest request) {
        StartProcessInEngineResponse response = camunda8IntegrationFeignClient.startProcessInstance(
                StartProcessInEngineRequest
                        .builder()
                        .processDefinitionKey(request.getProcessDefinitionKey())
                        .variables(request.getVariables())
                        .build()
        );

        return StartProcessInstanceResponse
                .builder()
                .processDefinitionKey(response.getProcessDefinitionKey())
                .bpmnProcessId(response.getBpmnProcessId())
                .version(response.getVersion())
                .processInstanceKey(response.getProcessInstanceKey())
                .status("STARTED")
                .build();
    }
}
