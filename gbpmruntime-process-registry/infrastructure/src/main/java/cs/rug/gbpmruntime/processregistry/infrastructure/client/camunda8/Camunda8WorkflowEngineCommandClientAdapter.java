package cs.rug.gbpmruntime.processregistry.infrastructure.client.camunda8;

import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceResponse;
import cs.rug.gbpmruntime.processregistry.application.out.engine.WorkflowEngineCommandClient;
import cs.rug.gbpmruntime.processregistry.infrastructure.client.camunda8.dto.deployprocess.DeployProcessToEngineRequestDto;
import cs.rug.gbpmruntime.processregistry.infrastructure.client.camunda8.dto.deployprocess.DeployProcessToEngineResponseDto;
import cs.rug.gbpmruntime.processregistry.infrastructure.client.camunda8.dto.startprocess.StartProcessInEngineRequestDto;
import cs.rug.gbpmruntime.processregistry.infrastructure.client.camunda8.dto.startprocess.StartProcessInEngineResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Camunda8WorkflowEngineCommandClientAdapter implements WorkflowEngineCommandClient {

    private final Camunda8IntegrationFeignClient camunda8IntegrationFeignClient;

    @Override
    public DeployProcessDefinitionResponse deployProcess(DeployProcessDefinitionRequest request) {
        DeployProcessToEngineResponseDto response = camunda8IntegrationFeignClient.deployProcessDefinition(
                DeployProcessToEngineRequestDto
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
        StartProcessInEngineResponseDto response = camunda8IntegrationFeignClient.startProcessInstance(
                StartProcessInEngineRequestDto
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
