package cs.rug.gbpmruntime.engineintegration.infrastructure.camunda8;

import cs.rug.gbpmruntime.engineintegration.api.exceptions.EngineProcessStartException;
import cs.rug.gbpmruntime.engineintegration.api.operations.startprocess.StartProcessInEngineResponse;
import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.ProcessInstanceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Camunda8ProcessInstanceClient {

    private final CamundaClient camundaClient;

    public StartProcessInEngineResponse start(String processDefinitionKey, Map<String, Object> variables) {
        try {
            ProcessInstanceEvent processInstanceEvent = camundaClient
                    .newCreateInstanceCommand()
                    .processDefinitionKey(Long.parseLong(processDefinitionKey))
                    .variables(normalizeVariables(variables))
                    .send()
                    .join();

            return StartProcessInEngineResponse
                    .builder()
                    .processDefinitionKey(String.valueOf(processInstanceEvent.getProcessDefinitionKey()))
                    .bpmnProcessId(processInstanceEvent.getBpmnProcessId())
                    .version(processInstanceEvent.getVersion())
                    .processInstanceKey(String.valueOf(processInstanceEvent.getProcessInstanceKey()))
                    .tenantId(processInstanceEvent.getTenantId())
                    .build();
        } catch (Exception exception) {
            throw new EngineProcessStartException("Failed to start process instance in Camunda 8.", exception);
        }
    }

    private Map<String, Object> normalizeVariables(Map<String, Object> variables) {
        if (variables == null) {
            return Collections.emptyMap();
        }

        return variables;
    }
}
