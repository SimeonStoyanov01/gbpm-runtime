package cs.rug.camunda8integration.application.processors.deployprocess;

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
        return camunda8CommandClient.deployProcess(request);
    }
}
