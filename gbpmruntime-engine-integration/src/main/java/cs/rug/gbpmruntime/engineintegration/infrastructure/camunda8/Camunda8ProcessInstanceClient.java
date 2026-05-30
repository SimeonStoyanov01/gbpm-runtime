package cs.rug.gbpmruntime.engineintegration.infrastructure.camunda8;

import cs.rug.gbpmruntime.engineintegration.api.operations.startprocess.StartProcessInEngineResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Camunda8ProcessInstanceClient {

    public StartProcessInEngineResponse start(String processDefinitionKey, Map<String, Object> variables) {
        return StartProcessInEngineResponse
                .builder()
                .processDefinitionKey(processDefinitionKey)
                .bpmnProcessId("TODO_CAMUNDA8_BPMN_PROCESS_ID")
                .version(1)
                .processInstanceKey("TODO_CAMUNDA8_PROCESS_INSTANCE_KEY")
                .tenantId(null)
                .build();
    }
}
