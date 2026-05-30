package cs.rug.gbpmruntime.processregistry.application.processors.startprocessinstance;

import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartProcessInstanceProcessor implements StartProcessInstanceOperation {

    @Override
    public StartProcessInstanceResponse process(StartProcessInstanceRequest request) {
        return StartProcessInstanceResponse
                .builder()
                .processDefinitionKey(request.getProcessDefinitionKey())
                .bpmnProcessId("TODO_BPMN_PROCESS_ID")
                .version(1)
                .processInstanceKey("TODO_PROCESS_INSTANCE_KEY")
                .status("PENDING_IMPLEMENTATION")
                .build();
    }
}
