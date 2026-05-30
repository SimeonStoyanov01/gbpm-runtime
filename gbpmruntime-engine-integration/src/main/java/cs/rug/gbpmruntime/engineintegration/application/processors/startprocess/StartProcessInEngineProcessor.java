package cs.rug.gbpmruntime.engineintegration.application.processors.startprocess;

import cs.rug.gbpmruntime.engineintegration.api.exceptions.UnsupportedWorkflowEngineException;
import cs.rug.gbpmruntime.engineintegration.api.operations.startprocess.StartProcessInEngineOperation;
import cs.rug.gbpmruntime.engineintegration.api.operations.startprocess.StartProcessInEngineRequest;
import cs.rug.gbpmruntime.engineintegration.api.operations.startprocess.StartProcessInEngineResponse;
import cs.rug.gbpmruntime.engineintegration.infrastructure.camunda8.Camunda8ProcessInstanceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartProcessInEngineProcessor implements StartProcessInEngineOperation {

    private static final String CAMUNDA_8 = "CAMUNDA_8";

    private final Camunda8ProcessInstanceClient camunda8ProcessInstanceClient;

    @Override
    public StartProcessInEngineResponse process(StartProcessInEngineRequest request) {
        validateTargetEngine(request.getTargetEngine());
        return camunda8ProcessInstanceClient.start(request.getProcessDefinitionKey(), request.getVariables());
    }

    private void validateTargetEngine(String targetEngine) {
        if (!CAMUNDA_8.equals(targetEngine)) {
            throw new UnsupportedWorkflowEngineException(targetEngine);
        }
    }
}
