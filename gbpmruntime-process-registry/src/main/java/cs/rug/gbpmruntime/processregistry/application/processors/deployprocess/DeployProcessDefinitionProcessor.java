package cs.rug.gbpmruntime.processregistry.application.processors.deployprocess;

import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployProcessDefinitionProcessor implements DeployProcessDefinitionOperation {

    @Override
    public DeployProcessDefinitionResponse process(DeployProcessDefinitionRequest request) {
        return DeployProcessDefinitionResponse
                .builder()
                .deploymentKey("TODO_DEPLOYMENT_KEY")
                .processDefinitionKey("TODO_PROCESS_DEFINITION_KEY")
                .bpmnProcessId("TODO_BPMN_PROCESS_ID")
                .version(1)
                .resourceName(request.getResourceName())
                .tenantId(null)
                .status("PENDING_IMPLEMENTATION")
                .build();
    }
}
