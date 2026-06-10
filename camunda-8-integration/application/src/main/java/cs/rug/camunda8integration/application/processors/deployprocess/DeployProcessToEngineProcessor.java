package cs.rug.camunda8integration.application.processors.deployprocess;

import cs.rug.camunda8integration.api.exceptions.EngineDeploymentException;
import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineOperation;
import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineRequest;
import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineResponse;
import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.DeploymentEvent;
import io.camunda.client.api.response.Process;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployProcessToEngineProcessor implements DeployProcessToEngineOperation {

    private final CamundaClient camundaClient;

    @Override
    public DeployProcessToEngineResponse process(DeployProcessToEngineRequest request) {
        try {
            DeploymentEvent deploymentEvent = camundaClient
                    .newDeployResourceCommand()
                    .addResourceBytes(request.getResourceContent(), request.getResourceName())
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
