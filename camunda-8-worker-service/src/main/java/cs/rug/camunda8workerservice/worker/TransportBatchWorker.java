package cs.rug.camunda8workerservice.worker;

import cs.rug.camunda8workerservice.client.MockOperationalServiceClient;
import cs.rug.camunda8workerservice.client.dto.CreateExecutionRunRequestDto;
import cs.rug.camunda8workerservice.client.dto.CreateExecutionRunResponseDto;
import cs.rug.camunda8workerservice.client.dto.ResourceUsageDto;
import cs.rug.camunda8workerservice.event.EngineTaskCompletedEvent;
import cs.rug.camunda8workerservice.event.ResourceUsageFact;
import cs.rug.camunda8workerservice.messaging.EngineTaskCompletedEventPublisher;
import cs.rug.camunda8workerservice.model.EngineExecutionContext;
import cs.rug.camunda8workerservice.model.WorkObjectModel;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransportBatchWorker {

    private static final String LEGACY_JOB_TYPE = "transport-batch";
    private static final String DEMO_JOB_TYPE = "demo-worker";
    private static final String DEFAULT_JOB_TYPE = "default-worker";
    private static final String ENGINE_TYPE = "CAMUNDA_8";
    private static final String EVENT_TYPE = "ENGINE_TASK_COMPLETED";
    private static final String COMPLETED_TASK_STATUS = "COMPLETED";

    private final MockOperationalServiceClient mockOperationalServiceClient;
    private final EngineTaskCompletedEventPublisher engineTaskCompletedEventPublisher;
    private final ObjectMapper objectMapper;

    @JobWorker(type = LEGACY_JOB_TYPE, name = "transport-worker-1")
    public void handleTransportBatch(JobClient jobClient, ActivatedJob job) {
        handleJob(jobClient, job);
    }

    @JobWorker(type = DEMO_JOB_TYPE, name = "demo-worker-1")
    public void handleDemoWorker(JobClient jobClient, ActivatedJob job) {
        handleJob(jobClient, job);
    }

    @JobWorker(type = DEFAULT_JOB_TYPE, name = "default-worker-1")
    public void handleDefaultWorker(JobClient jobClient, ActivatedJob job) {
        handleJob(jobClient, job);
    }

    private void handleJob(JobClient jobClient, ActivatedJob job) {
        CreateExecutionRunResponseDto executionRun = executeOperationalTask(job);

        if (!COMPLETED_TASK_STATUS.equals(executionRun.getStatus())) {
            throw new IllegalStateException("Operational execution did not complete successfully: "
                    + executionRun.getStatus());
        }

        EngineTaskCompletedEvent event = buildEngineTaskCompletedEvent(job, executionRun);

        jobClient
                .newCompleteCommand(job.getKey())
                .variables(Map.of("transportCompleted", true))
                .send()
                .join();

        publishEvent(event);

        log.info(
                "Camunda job completed by worker: eventId={}, processInstanceKey={}, bpmnElementId={}, jobKey={}",
                event.getEventId(),
                job.getProcessInstanceKey(),
                job.getElementId(),
                job.getKey()
        );
    }

    private CreateExecutionRunResponseDto executeOperationalTask(ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();

        return mockOperationalServiceClient.createExecutionRun(CreateExecutionRunRequestDto
                .builder()
                .orderId(readOptionalString(variables, "orderId", String.valueOf(job.getProcessInstanceKey())))
                .bpmnElementId(job.getElementId())
                .workObject(readRequiredValue(variables, "workObject", WorkObjectModel.class))
                .build());
    }

    private EngineTaskCompletedEvent buildEngineTaskCompletedEvent(
            ActivatedJob job,
            CreateExecutionRunResponseDto executionRun
    ) {
        return EngineTaskCompletedEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EVENT_TYPE)
                .occurredAt(Instant.now())
                .execution(EngineExecutionContext
                        .builder()
                        .engineType(ENGINE_TYPE)
                        .processDefinitionKey(job.getProcessDefinitionKey())
                        .bpmnProcessId(job.getBpmnProcessId())
                        .processInstanceKey(job.getProcessInstanceKey())
                        .elementInstanceKey(job.getElementInstanceKey())
                        .bpmnElementId(job.getElementId())
                        .build())
                .taskStatus(executionRun.getStatus())
                .resourceUsages(toResourceUsageFacts(executionRun.getResourceUsages()))
                .build();
    }

    private List<ResourceUsageFact> toResourceUsageFacts(List<ResourceUsageDto> resourceUsages) {
        if (resourceUsages == null) {
            return List.of();
        }

        return resourceUsages
                .stream()
                .map(resourceUsage -> ResourceUsageFact
                        .builder()
                        .resourceName(resourceUsage.getResourceName())
                        .timeUsed(resourceUsage.getTimeUsed())
                        .unit(resourceUsage.getUnit())
                        .build())
                .toList();
    }

    private void publishEvent(EngineTaskCompletedEvent event) {
        try {
            engineTaskCompletedEventPublisher.publish(event);
        } catch (Exception exception) {
            log.warn(
                    "Failed to publish engine task completed event after Camunda job completion: eventId={}",
                    event.getEventId(),
                    exception
            );
        }
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
