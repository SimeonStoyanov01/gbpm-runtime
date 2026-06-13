package cs.rug.camunda8integration.application.processors.startprocess;

import cs.rug.camunda8integration.api.exceptions.EngineProcessStartException;
import cs.rug.camunda8integration.api.operations.startprocess.StartProcessInEngineOperation;
import cs.rug.camunda8integration.api.operations.startprocess.StartProcessInEngineRequest;
import cs.rug.camunda8integration.api.operations.startprocess.StartProcessInEngineResponse;
import cs.rug.camunda8integration.application.out.Camunda8CommandClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartProcessInEngineProcessor implements StartProcessInEngineOperation {

    private final Camunda8CommandClient camunda8CommandClient;

    @Override
    public StartProcessInEngineResponse process(StartProcessInEngineRequest request) {
        try {
            return camunda8CommandClient.startProcess(request);
        } catch (Exception exception) {
            throw new EngineProcessStartException("Failed to start process instance in Camunda 8.", exception);
        }
    }
}
