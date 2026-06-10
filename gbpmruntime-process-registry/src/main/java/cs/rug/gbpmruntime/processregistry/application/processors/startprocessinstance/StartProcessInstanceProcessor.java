package cs.rug.gbpmruntime.processregistry.application.processors.startprocessinstance;

import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceResponse;
import cs.rug.gbpmruntime.processregistry.infrastructure.clients.Camunda8IntegrationClient;
import cs.rug.gbpmruntime.processregistry.infrastructure.clients.dto.startprocess.StartProcessInEngineRequest;
import cs.rug.gbpmruntime.processregistry.infrastructure.clients.dto.startprocess.StartProcessInEngineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartProcessInstanceProcessor implements StartProcessInstanceOperation {

    private final Camunda8IntegrationClient camunda8IntegrationClient;

    @Override
    public StartProcessInstanceResponse process(StartProcessInstanceRequest request) {
        StartProcessInEngineRequest startProcessInEngineRequest = StartProcessInEngineRequest
                .builder()
                .processDefinitionKey(request.getProcessDefinitionKey())
                .variables(request.getVariables())
                .build();

        StartProcessInEngineResponse startProcessInEngineResponse = camunda8IntegrationClient
                .startProcessInstance(startProcessInEngineRequest);

        return StartProcessInstanceResponse
                .builder()
                .processDefinitionKey(startProcessInEngineResponse.getProcessDefinitionKey())
                .bpmnProcessId(startProcessInEngineResponse.getBpmnProcessId())
                .version(startProcessInEngineResponse.getVersion())
                .processInstanceKey(startProcessInEngineResponse.getProcessInstanceKey())
                .status("STARTED")
                .build();
    }
}
