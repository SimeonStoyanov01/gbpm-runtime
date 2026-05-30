package cs.rug.gbpmruntime.engineintegration.application.processors.deployprocess;

import cs.rug.gbpmruntime.engineintegration.api.exceptions.UnsupportedWorkflowEngineException;
import cs.rug.gbpmruntime.engineintegration.api.operations.deployprocess.DeployProcessToEngineOperation;
import cs.rug.gbpmruntime.engineintegration.api.operations.deployprocess.DeployProcessToEngineRequest;
import cs.rug.gbpmruntime.engineintegration.api.operations.deployprocess.DeployProcessToEngineResponse;
import cs.rug.gbpmruntime.engineintegration.infrastructure.camunda8.Camunda8DeploymentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployProcessToEngineProcessor implements DeployProcessToEngineOperation {

    private static final String CAMUNDA_8 = "CAMUNDA_8";

    private final Camunda8DeploymentClient camunda8DeploymentClient;

    @Override
    public DeployProcessToEngineResponse process(DeployProcessToEngineRequest request) {
        validateTargetEngine(request.getTargetEngine());
        return camunda8DeploymentClient.deploy(request.getResourceName(), request.getResourceContent());
    }

    private void validateTargetEngine(String targetEngine) {
        if (!CAMUNDA_8.equals(targetEngine)) {
            throw new UnsupportedWorkflowEngineException(targetEngine);
        }
    }
}
