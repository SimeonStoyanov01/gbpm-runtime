package cs.rug.gbpmruntime.engineintegration.infrastructure.camunda8;

import cs.rug.gbpmruntime.engineintegration.api.exceptions.EngineDeploymentException;
import cs.rug.gbpmruntime.engineintegration.api.operations.deployprocess.DeployProcessToEngineResponse;
import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.DeploymentEvent;
import io.camunda.client.api.response.Process;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Camunda8DeploymentClient {

    private final CamundaClient camundaClient;

    public DeployProcessToEngineResponse deploy(String resourceName, byte[] resourceContent) {
        try {
            DeploymentEvent deploymentEvent = camundaClient
                    .newDeployResourceCommand()
                    .addResourceBytes(resourceContent, resourceName)
                    .send()
                    .join();

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
        } catch (EngineDeploymentException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new EngineDeploymentException("Failed to deploy BPMN resource to Camunda 8.", exception);
        }
    }
}
