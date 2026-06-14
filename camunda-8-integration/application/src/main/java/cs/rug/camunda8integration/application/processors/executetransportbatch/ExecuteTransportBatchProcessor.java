package cs.rug.camunda8integration.application.processors.executetransportbatch;

import cs.rug.camunda8integration.api.model.EngineExecutionContext;
import cs.rug.camunda8integration.api.events.enginetaskcompleted.EngineTaskCompletedEvent;
import cs.rug.camunda8integration.api.operations.executetransportbatch.ExecuteTransportBatchOperation;
import cs.rug.camunda8integration.api.operations.executetransportbatch.ExecuteTransportBatchRequest;
import cs.rug.camunda8integration.api.operations.executetransportbatch.ExecuteTransportBatchResponse;
import cs.rug.camunda8integration.application.model.OperationalExecutionRequest;
import cs.rug.camunda8integration.application.model.OperationalExecutionResult;
import cs.rug.camunda8integration.application.out.OperationalExecutionClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExecuteTransportBatchProcessor implements ExecuteTransportBatchOperation {

    private static final String ENGINE_TYPE = "CAMUNDA_8";
    private static final String EVENT_TYPE = "ENGINE_TASK_COMPLETED";
    private static final String CONTRACT_VERSION = "1.0";
    private static final String COMPLETED_TASK_STATUS = "COMPLETED";

    private final OperationalExecutionClient operationalExecutionClient;

    @Override
    public ExecuteTransportBatchResponse process(ExecuteTransportBatchRequest request) {
        OperationalExecutionResult executionResult = operationalExecutionClient.execute(OperationalExecutionRequest
                .builder()
                .orderId(request.getOrderId())
                .bpmnElementId(request.getEngineExecutionContext().getBpmnElementId())
                .workObject(request.getWorkObject())
                .assignedResources(request.getAssignedResources())
                .build());

        if (!COMPLETED_TASK_STATUS.equals(executionResult.getStatus())) {
            throw new IllegalStateException("Operational execution did not complete successfully: "
                    + executionResult.getStatus());
        }

        EngineTaskCompletedEvent event = buildEngineTaskCompletedEvent(request, executionResult);

        return ExecuteTransportBatchResponse
                .builder()
                .eventId(event.getEventId())
                .taskStatus(event.getTaskStatus())
                .completionVariables(Map.of("transportCompleted", true))
                .engineTaskCompletedEvent(event)
                .build();
    }

    private EngineTaskCompletedEvent buildEngineTaskCompletedEvent(
            ExecuteTransportBatchRequest request,
            OperationalExecutionResult executionResult
    ) {
        return EngineTaskCompletedEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EVENT_TYPE)
                .contractVersion(CONTRACT_VERSION)
                .occurredAt(Instant.now())
                .execution(EngineExecutionContext
                        .builder()
                        .engineType(ENGINE_TYPE)
                        .processDefinitionKey(request.getEngineExecutionContext().getProcessDefinitionKey())
                        .bpmnProcessId(request.getEngineExecutionContext().getBpmnProcessId())
                        .processInstanceKey(request.getEngineExecutionContext().getProcessInstanceKey())
                        .elementInstanceKey(request.getEngineExecutionContext().getElementInstanceKey())
                        .bpmnElementId(request.getEngineExecutionContext().getBpmnElementId())
                        .build())
                .taskStatus(executionResult.getStatus())
                .resourceUsages(executionResult.getResourceUsages())
                .build();
    }
}
