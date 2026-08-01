package cs.rug.camunda8integration.infrastructure.client.camunda8;

import cs.rug.camunda8integration.api.exceptions.EngineDeploymentException;
import cs.rug.camunda8integration.api.exceptions.EngineProcessStartException;
import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineRequest;
import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineResponse;
import cs.rug.camunda8integration.api.operations.startprocess.StartProcessInEngineRequest;
import cs.rug.camunda8integration.api.operations.startprocess.StartProcessInEngineResponse;
import cs.rug.camunda8integration.application.out.Camunda8CommandClient;
import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.DeploymentEvent;
import io.camunda.client.api.response.Process;
import io.camunda.client.api.response.ProcessInstanceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Camunda8CommandClientAdapter implements Camunda8CommandClient {

    private final CamundaClient camundaClient;

    @Override
    public DeployProcessToEngineResponse deployProcess(DeployProcessToEngineRequest request) {
        DeploymentEvent deploymentEvent;
        try {
            deploymentEvent = camundaClient
                    .newDeployResourceCommand()
                    .addResourceBytes(request.getResourceContent(), request.getResourceName())
                    .send()
                    .join();
        } catch (RuntimeException exception) {
            throw new EngineDeploymentException("Failed to deploy BPMN resource to Camunda 8.", exception);
        }

        if (deploymentEvent.getProcesses().isEmpty()) {
            throw new EngineDeploymentException("Camunda deployment did not return a process definition.");
        }

        Process process = deploymentEvent.getProcesses().getFirst();

        return DeployProcessToEngineResponse
                .builder()
                .deploymentKey(String.valueOf(deploymentEvent.getKey()))
                .processDefinitionKey(String.valueOf(process.getProcessDefinitionKey()))
                .bpmnProcessId(process.getBpmnProcessId())
                .version(process.getVersion())
                .resourceName(process.getResourceName())
                .tenantId(process.getTenantId())
                .build();
    }

    @Override
    public StartProcessInEngineResponse startProcess(StartProcessInEngineRequest request) {
        ProcessInstanceEvent processInstanceEvent;
        try {
            processInstanceEvent = camundaClient
                    .newCreateInstanceCommand()
                    .processDefinitionKey(Long.parseLong(request.getProcessDefinitionKey()))
                    .variables(normalizeVariables(request.getVariables()))
                    .send()
                    .join();
        } catch (RuntimeException exception) {
            throw new EngineProcessStartException("Failed to start process instance in Camunda 8.", exception);
        }

        return StartProcessInEngineResponse
                .builder()
                .processDefinitionKey(String.valueOf(processInstanceEvent.getProcessDefinitionKey()))
                .bpmnProcessId(processInstanceEvent.getBpmnProcessId())
                .version(processInstanceEvent.getVersion())
                .processInstanceKey(String.valueOf(processInstanceEvent.getProcessInstanceKey()))
                .tenantId(processInstanceEvent.getTenantId())
                .build();
    }

    private Map<String, Object> normalizeVariables(Map<String, Object> variables) {
        if (variables == null) {
            return Collections.emptyMap();
        }

        return variables;
    }
}
