package cs.rug.camunda8integration.application.out;

import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineRequest;
import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineResponse;
import cs.rug.camunda8integration.api.operations.startprocess.StartProcessInEngineRequest;
import cs.rug.camunda8integration.api.operations.startprocess.StartProcessInEngineResponse;

public interface Camunda8CommandClient {

    DeployProcessToEngineResponse deployProcess(DeployProcessToEngineRequest request);

    StartProcessInEngineResponse startProcess(StartProcessInEngineRequest request);
}
