package cs.rug.camunda8integration.infrastructure.worker;

import cs.rug.camunda8integration.api.events.enginetaskcompleted.EngineExecutionContext;
import cs.rug.camunda8integration.api.events.enginetaskcompleted.EngineTaskCompletedEvent;
import cs.rug.camunda8integration.api.events.enginetaskcompleted.ResourceUsageFact;
import cs.rug.camunda8integration.api.operations.publishenginetaskcompleted.PublishEngineTaskCompletedEventOperation;
import cs.rug.camunda8integration.api.operations.publishenginetaskcompleted.PublishEngineTaskCompletedEventRequest;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransportBatchWorker {

    private static final String ENGINE_TYPE = "CAMUNDA_8";
    private static final String JOB_TYPE = "transport-batch";
    private static final String WORKER_NAME = "transport-worker-1";
    private static final String EVENT_TYPE = "ENGINE_TASK_COMPLETED";
    private static final String CONTRACT_VERSION = "1.0";
    private static final String COMPLETED_TASK_STATUS = "COMPLETED";

    private final PublishEngineTaskCompletedEventOperation publishEngineTaskCompletedEventOperation;

    @JobWorker(type = JOB_TYPE, name = WORKER_NAME)
    public void handleTransportBatch(JobClient jobClient, ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        Map<String, Object> resourceUsagePlan = readMap(variables, "resourceUsagePlan");
        List<ResourceUsageFact> resourceUsages = readResourceUsages(resourceUsagePlan);
        if (resourceUsages.isEmpty()) {
            log.warn(
                    "Transport batch job has no resource usage facts: processInstanceKey={}, bpmnElementId={}, jobKey={}",
                    job.getProcessInstanceKey(),
                    job.getElementId(),
                    job.getKey()
            );
        }

        Map<String, Object> businessOutput = buildBusinessOutput();
        jobClient
                .newCompleteCommand(job.getKey())
                .variables(businessOutput)
                .send()
                .join();

        EngineTaskCompletedEvent event = buildEngineTaskCompletedEvent(
                job,
                resourceUsages
        );
        publishEvent(event);

        log.info(
                "Transport batch job completed: eventId={}, processInstanceKey={}, bpmnElementId={}, jobKey={}",
                event.getEventId(),
                event.getExecution().getProcessInstanceKey(),
                event.getExecution().getBpmnElementId(),
                event.getExecution().getJobKey()
        );
    }

    private Map<String, Object> buildBusinessOutput() {
        Map<String, Object> businessOutput = new LinkedHashMap<>();
        businessOutput.put("transportCompleted", true);
        return businessOutput;
    }

    private EngineTaskCompletedEvent buildEngineTaskCompletedEvent(
            ActivatedJob job,
            List<ResourceUsageFact> resourceUsages
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
                        .processDefinitionKey(job.getProcessDefinitionKey())
                        .bpmnProcessId(job.getBpmnProcessId())
                        .processInstanceKey(job.getProcessInstanceKey())
                        .elementInstanceKey(job.getElementInstanceKey())
                        .bpmnElementId(job.getElementId())
                        .jobKey(job.getKey())
                        .jobType(job.getType())
                        .workerName(WORKER_NAME)
                        .build())
                .taskStatus(COMPLETED_TASK_STATUS)
                .resourceUsages(resourceUsages)
                .build();
    }

    private void publishEvent(EngineTaskCompletedEvent event) {
        try {
            publishEngineTaskCompletedEventOperation.process(PublishEngineTaskCompletedEventRequest
                    .builder()
                    .event(event)
                    .build());
        } catch (Exception exception) {
            log.warn(
                    "Failed to publish engine task completed event after Camunda job completion: eventId={}, processInstanceKey={}, bpmnElementId={}, jobKey={}",
                    event.getEventId(),
                    event.getExecution().getProcessInstanceKey(),
                    event.getExecution().getBpmnElementId(),
                    event.getExecution().getJobKey(),
                    exception
            );
        }
    }

    private Map<String, Object> readMap(Map<String, Object> variables, String name) {
        if (variables == null) {
            return Map.of();
        }

        Object value = variables.get(name);
        if (value instanceof Map<?, ?> map) {
            Map<String, Object> valuesByName = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getKey() instanceof String key) {
                    valuesByName.put(key, entry.getValue());
                }
            }
            return valuesByName;
        }

        return Map.of();
    }

    private List<ResourceUsageFact> readResourceUsages(Map<String, Object> resourceUsagePlan) {
        Object value = resourceUsagePlan.get("resourcesUsed");
        if (!(value instanceof List<?> resourceUsageValues)) {
            return List.of();
        }

        List<ResourceUsageFact> resourceUsages = new ArrayList<>();
        for (Object resourceUsageValue : resourceUsageValues) {
            if (resourceUsageValue instanceof Map<?, ?> resourceUsage) {
                ResourceUsageFact normalizedResourceUsage = normalizeResourceUsage(resourceUsage);
                if (normalizedResourceUsage != null) {
                    resourceUsages.add(normalizedResourceUsage);
                }
            }
        }

        return resourceUsages;
    }

    private ResourceUsageFact normalizeResourceUsage(Map<?, ?> resourceUsage) {
        String resourceName = readString(resourceUsage, "resourceName");
        Double timeUsed = readDouble(resourceUsage, "timeUsed");
        String unit = readString(resourceUsage, "unit");

        if (resourceName == null || timeUsed == null || unit == null) {
            log.warn("Ignoring incomplete resource usage fact: {}", resourceUsage);
            return null;
        }

        return ResourceUsageFact
                .builder()
                .resourceName(resourceName)
                .timeUsed(timeUsed)
                .unit(unit)
                .build();
    }

    private String readString(Map<?, ?> variables, String name) {
        Object value = variables.get(name);
        if (value == null) {
            return null;
        }

        String stringValue = value.toString();
        return stringValue.isBlank() ? null : stringValue;
    }

    private Double readDouble(Map<?, ?> variables, String name) {
        Object value = variables.get(name);
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value instanceof String stringValue && !stringValue.isBlank()) {
            return Double.parseDouble(stringValue);
        }

        return null;
    }
}
