package cs.rug.processregistryservice.application.out.engine;

import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionRequest;
import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.processregistryservice.api.operations.startprocessinstance.StartProcessInstanceRequest;
import cs.rug.processregistryservice.api.operations.startprocessinstance.StartProcessInstanceResponse;

public interface WorkflowEngineCommandClient {

    DeployProcessDefinitionResponse deployProcess(DeployProcessDefinitionRequest request);

    StartProcessInstanceResponse startProcessInstance(StartProcessInstanceRequest request);
}
