package cs.rug.camunda8workerservice.worker;

import cs.rug.camunda8workerservice.client.MockOperationalServiceClient;
import cs.rug.camunda8workerservice.client.dto.CreateExecutionRunRequest;
import cs.rug.camunda8workerservice.client.dto.CreateExecutionRunResponse;
import cs.rug.camunda8workerservice.event.EngineTaskCompletedEvent;
import cs.rug.camunda8workerservice.messaging.EngineTaskCompletedEventPublisher;
import cs.rug.camunda8workerservice.model.EngineExecutionContext;
import cs.rug.camunda8workerservice.model.WorkObject;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenericCamundaWorker {

    private final MockOperationalServiceClient mockOperationalServiceClient;
    private final EngineTaskCompletedEventPublisher engineTaskCompletedEventPublisher;
    private final CamundaWorkerProperties camundaWorkerProperties;
    private final ObjectMapper objectMapper;

    @JobWorker(
            type = "default-worker",
            name = "generic-camunda-worker",
            autoComplete = false
    )
    public void handleJob(JobClient jobClient, ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        WorkObject workObject = readRequiredValue(
                variables,
                camundaWorkerProperties.getWorkObjectVariable(),
                WorkObject.class
        );
        CreateExecutionRunResponse executionRun = createExecutionRun(job, variables, workObject);

        if (!isSuccessful(executionRun)) {
            throw new IllegalStateException("Operational execution did not complete successfully: "
                    + executionRun.getStatus());
        }

        EngineTaskCompletedEvent event = buildEngineTaskCompletedEvent(job, executionRun, workObject.getType());
        engineTaskCompletedEventPublisher.publish(event);

        jobClient
                .newCompleteCommand(job.getKey())
                .send()
                .join();

        log.info(
                "Camunda job completed by generic worker: processInstanceKey={}, bpmnElementId={}, jobKey={}",
                job.getProcessInstanceKey(),
                job.getElementId(),
                job.getKey()
        );
    }

    private CreateExecutionRunResponse createExecutionRun(
            ActivatedJob job,
            Map<String, Object> variables,
            WorkObject workObject
    ) {
        return mockOperationalServiceClient.createExecutionRun(CreateExecutionRunRequest
                .builder()
                .orderId(readOptionalString(
                        variables,
                        camundaWorkerProperties.getOrderIdVariable(),
                        String.valueOf(job.getProcessInstanceKey())
                ))
                .bpmnElementId(job.getElementId())
                .workObject(workObject)
                .build());
    }

    private boolean isSuccessful(CreateExecutionRunResponse executionRun) {
        return camundaWorkerProperties.getSuccessfulStatus().equals(executionRun.getStatus());
    }

    private EngineTaskCompletedEvent buildEngineTaskCompletedEvent(
            ActivatedJob job,
            CreateExecutionRunResponse executionRun,
            String workObjectType
    ) {
        return EngineTaskCompletedEvent
                .builder()
                .execution(EngineExecutionContext
                        .builder()
                        .engineType(camundaWorkerProperties.getEngineType())
                        .processDefinitionKey(job.getProcessDefinitionKey())
                        .bpmnProcessId(job.getBpmnProcessId())
                        .processInstanceKey(job.getProcessInstanceKey())
                        .elementInstanceKey(job.getElementInstanceKey())
                        .bpmnElementId(job.getElementId())
                        .build())
                .workObjectType(workObjectType)
                .resourceUsages(executionRun.getResourceUsages() == null
                        ? List.of()
                        : executionRun.getResourceUsages())
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
