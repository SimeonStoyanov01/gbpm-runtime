package cs.rug.camunda8integration.infrastructure.worker;

import cs.rug.camunda8integration.api.model.EngineExecutionContext;
import cs.rug.camunda8integration.api.model.WorkObjectModel;
import cs.rug.camunda8integration.api.operations.executetransportbatch.ExecuteTransportBatchOperation;
import cs.rug.camunda8integration.api.operations.executetransportbatch.ExecuteTransportBatchRequest;
import cs.rug.camunda8integration.api.operations.executetransportbatch.ExecuteTransportBatchResponse;
import cs.rug.camunda8integration.application.out.EngineTaskCompletedEventPublisher;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransportBatchWorker {

    private static final String LEGACY_JOB_TYPE = "transport-batch";
    private static final String DEFAULT_JOB_TYPE = "default-worker";

    private final ExecuteTransportBatchOperation executeTransportBatchOperation;
    private final EngineTaskCompletedEventPublisher engineTaskCompletedEventPublisher;
    private final ObjectMapper objectMapper;

    @JobWorker(type = LEGACY_JOB_TYPE, name = "transport-worker-1")
    public void handleTransportBatch(JobClient jobClient, ActivatedJob job) {
        handleJob(jobClient, job);
    }

    @JobWorker(type = DEFAULT_JOB_TYPE, name = "default-worker-1")
    public void handleDefaultWorker(JobClient jobClient, ActivatedJob job) {
        handleJob(jobClient, job);
    }

    private void handleJob(JobClient jobClient, ActivatedJob job) {
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
            engineTaskCompletedEventPublisher.publish(response.getEngineTaskCompletedEvent());
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
                .engineExecutionContext(EngineExecutionContext
                        .builder()
                        .bpmnElementId(job.getElementId())
                        .bpmnProcessId(job.getBpmnProcessId())
                        .elementInstanceKey(job.getElementInstanceKey())
                        .processInstanceKey(job.getProcessInstanceKey())
                        .processDefinitionKey(job.getProcessDefinitionKey())
                        .build())
                .orderId(readOptionalString(variables, "orderId", String.valueOf(job.getProcessInstanceKey())))
                .workObject(readRequiredValue(variables, "workObject", WorkObjectModel.class))
                .build();
    }

    private String readOptionalString(Map<String, Object> variables, String name, String fallback) {
        Object value = variables.get(name);
        if (value == null || value.toString().isBlank()) {
            return fallback;
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
}
