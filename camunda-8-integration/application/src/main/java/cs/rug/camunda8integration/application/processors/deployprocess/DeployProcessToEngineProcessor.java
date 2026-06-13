package cs.rug.camunda8integration.application.processors.deployprocess;

import cs.rug.camunda8integration.api.exceptions.EngineDeploymentException;
import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineOperation;
import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineRequest;
import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineResponse;
import cs.rug.camunda8integration.application.out.Camunda8CommandClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployProcessToEngineProcessor implements DeployProcessToEngineOperation {

    private final Camunda8CommandClient camunda8CommandClient;

    @Override
    public DeployProcessToEngineResponse process(DeployProcessToEngineRequest request) {
        try {
            return camunda8CommandClient.deployProcess(request);
        } catch (EngineDeploymentException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new EngineDeploymentException("Failed to deploy BPMN resource to Camunda 8.", exception);
        }
    }
}
