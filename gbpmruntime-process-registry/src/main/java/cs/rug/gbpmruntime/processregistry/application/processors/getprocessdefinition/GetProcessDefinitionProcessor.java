package cs.rug.gbpmruntime.processregistry.application.processors.getprocessdefinition;

import cs.rug.gbpmruntime.processregistry.api.model.ProcessDefinitionRegistrationModel;
import cs.rug.gbpmruntime.processregistry.api.operations.getprocessdefinition.GetProcessDefinitionOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.getprocessdefinition.GetProcessDefinitionRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.getprocessdefinition.GetProcessDefinitionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProcessDefinitionProcessor implements GetProcessDefinitionOperation {

    @Override
    public GetProcessDefinitionResponse process(GetProcessDefinitionRequest request) {
        ProcessDefinitionRegistrationModel registration = ProcessDefinitionRegistrationModel
                .builder()
                .deploymentKey("TODO_DEPLOYMENT_KEY")
                .processDefinitionKey(request.getProcessDefinitionKey())
                .bpmnProcessId("TODO_BPMN_PROCESS_ID")
                .version(1)
                .resourceName("TODO_RESOURCE_NAME")
                .tenantId(null)
                .status("PENDING_IMPLEMENTATION")
                .build();
        return GetProcessDefinitionResponse
                .builder()
                .registration(registration)
                .build();
    }
}
