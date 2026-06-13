package cs.rug.camunda8integration.infrastructure.worker;

import cs.rug.camunda8integration.api.model.AssignedResourceModel;
import cs.rug.camunda8integration.api.model.WorkObjectModel;
import cs.rug.camunda8integration.api.operations.executetransportbatch.ExecuteTransportBatchOperation;
import cs.rug.camunda8integration.api.operations.executetransportbatch.ExecuteTransportBatchRequest;
import cs.rug.camunda8integration.api.operations.executetransportbatch.ExecuteTransportBatchResponse;
import cs.rug.camunda8integration.api.operations.publishenginetaskcompleted.PublishEngineTaskCompletedEventOperation;
import cs.rug.camunda8integration.api.operations.publishenginetaskcompleted.PublishEngineTaskCompletedEventRequest;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransportBatchWorker {

    private static final String JOB_TYPE = "transport-batch";
    private static final String WORKER_NAME = "transport-worker-1";

    private final ExecuteTransportBatchOperation executeTransportBatchOperation;
    private final PublishEngineTaskCompletedEventOperation publishEngineTaskCompletedEventOperation;
    private final ObjectMapper objectMapper;

    @JobWorker(type = JOB_TYPE, name = WORKER_NAME)
    public void handleTransportBatch(JobClient jobClient, ActivatedJob job) {
        ExecuteTransportBatchResponse response = executeTransportBatchOperation.process(
                buildRequest(job)
        );

        jobClient
                .newCompleteCommand(job.getKey())
                .variables(response.getCompletionVariables())
                .send()
                .join();

        publishEvent(response);

        log.info(
                "Transport batch job completed: eventId={}, processInstanceKey={}, bpmnElementId={}, jobKey={}",
                response.getEventId(),
                job.getProcessInstanceKey(),
                job.getElementId(),
                job.getKey()
        );
    }

    private void publishEvent(ExecuteTransportBatchResponse response) {
        try {
            publishEngineTaskCompletedEventOperation.process(PublishEngineTaskCompletedEventRequest
                    .builder()
                    .event(response.getEngineTaskCompletedEvent())
                    .build());
        } catch (Exception exception) {
            log.warn(
                    "Failed to publish engine task completed event after Camunda job completion: eventId={}",
                    response.getEventId(),
                    exception
            );
        }
    }

    private ExecuteTransportBatchRequest buildRequest(ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();

        return ExecuteTransportBatchRequest
                .builder()
                .processDefinitionKey(job.getProcessDefinitionKey())
                .bpmnProcessId(job.getBpmnProcessId())
                .processInstanceKey(job.getProcessInstanceKey())
                .elementInstanceKey(job.getElementInstanceKey())
                .bpmnElementId(job.getElementId())
                .jobKey(job.getKey())
                .jobType(job.getType())
                .workerName(WORKER_NAME)
                .orderId(readRequiredString(variables, "orderId"))
                .workObject(readRequiredValue(variables, "workObject", WorkObjectModel.class))
                .assignedResources(readAssignedResources(variables, job.getElementId()))
                .build();
    }

    private String readRequiredString(Map<String, Object> variables, String name) {
        Object value = variables.get(name);
        if (value == null || value.toString().isBlank()) {
            throw new IllegalArgumentException("Missing required process variable: " + name);
        }

        return value.toString();
    }

    private <T> T readRequiredValue(Map<String, Object> variables, String name, Class<T> valueType) {
        Object value = variables.get(name);
        if (value == null) {
            throw new IllegalArgumentException("Missing required process variable: " + name);
        }

        return objectMapper.convertValue(value, valueType);
    }

    private List<AssignedResourceModel> readAssignedResources(
            Map<String, Object> variables,
            String bpmnElementId
    ) {
        Object value = variables.get("resourceAssignments");
        if (!(value instanceof Map<?, ?> assignmentsByElement)) {
            throw new IllegalArgumentException("Missing required process variable: resourceAssignments");
        }

        Object assignedResources = assignmentsByElement.get(bpmnElementId);
        if (assignedResources == null) {
            throw new IllegalArgumentException("No resource assignment found for BPMN element: " + bpmnElementId);
        }

        return Arrays.asList(objectMapper.convertValue(assignedResources, AssignedResourceModel[].class));
    }
}
