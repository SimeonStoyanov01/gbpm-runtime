package cs.rug.gbpmruntime.processregistry.application.out.engine;

import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceResponse;

public interface WorkflowEngineCommandClient {

    DeployProcessDefinitionResponse deployProcess(DeployProcessDefinitionRequest request);

    StartProcessInstanceResponse startProcessInstance(StartProcessInstanceRequest request);
}
