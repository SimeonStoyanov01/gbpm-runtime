package cs.rug.camunda8integration.application.processors.startprocess;

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
        return camunda8CommandClient.startProcess(request);
    }
}
