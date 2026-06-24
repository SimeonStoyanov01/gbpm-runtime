package cs.rug.gbpmruntime.processregistry.application.processors.startprocessinstance;

import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceResponse;
import cs.rug.gbpmruntime.processregistry.application.out.engine.WorkflowEngineCommandClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartProcessInstanceProcessor implements StartProcessInstanceOperation {

    private final WorkflowEngineCommandClient workflowEngineCommandClient;

    @Override
    public StartProcessInstanceResponse process(StartProcessInstanceRequest request) {
        return workflowEngineCommandClient.startProcessInstance(request);
    }
}
