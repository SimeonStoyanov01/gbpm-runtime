package cs.rug.gbpmruntime.engineintegration.infrastructure.camunda8;

import cs.rug.gbpmruntime.engineintegration.api.operations.deployprocess.DeployProcessToEngineResponse;
import org.springframework.stereotype.Component;

@Component
public class Camunda8DeploymentClient {

    public DeployProcessToEngineResponse deploy(String resourceName, byte[] resourceContent) {
        return DeployProcessToEngineResponse
                .builder()
                .deploymentKey("TODO_CAMUNDA8_DEPLOYMENT_KEY")
                .processDefinitionKey("TODO_CAMUNDA8_PROCESS_DEFINITION_KEY")
                .bpmnProcessId("TODO_CAMUNDA8_BPMN_PROCESS_ID")
                .version(1)
                .resourceName(resourceName)
                .tenantId(null)
                .build();
    }
}
