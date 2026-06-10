package cs.rug.camunda8integration.infrastructure.worker;

import cs.rug.camunda8integration.api.events.EngineTaskCompletedEvent;
import cs.rug.camunda8integration.api.operations.publishenginetaskcompleted.PublishEngineTaskCompletedEventOperation;
import cs.rug.camunda8integration.api.operations.publishenginetaskcompleted.PublishEngineTaskCompletedEventRequest;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedHashMap;
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
    private static final String RESOURCE_ID = "TRANSPORT_UNIT_01";
    private static final String RESOURCE_TYPE = "transport-unit";
    private static final int DISTANCE_METERS = 750;
    private static final int DAMAGED_UNITS = 2;
    private static final double LOAD_FACTOR = 0.65;

    private final PublishEngineTaskCompletedEventOperation publishEngineTaskCompletedEventOperation;

    @JobWorker(type = JOB_TYPE, name = WORKER_NAME)
    public void handleTransportBatch(JobClient jobClient, ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        Map<String, Object> productionRequest = readMap(variables, "productionRequest");
        int quantityRequested = readInt(productionRequest, "quantityRequested");
        double batchMassKg = readDouble(productionRequest, "batchMassKg");

        Map<String, Object> businessOutput = buildBusinessOutput(quantityRequested);
        jobClient
                .newCompleteCommand(job.getKey())
                .variables(businessOutput)
                .send()
                .join();

        EngineTaskCompletedEvent event = buildEngineTaskCompletedEvent(
                job,
                variables,
                businessOutput,
                quantityRequested,
                batchMassKg
        );
        publishEvent(event);

        log.info(
                "Transport batch job completed: eventId={}, processInstanceKey={}, bpmnElementId={}, jobKey={}",
                event.getEventId(),
                event.getProcessInstanceKey(),
                event.getBpmnElementId(),
                event.getJobKey()
        );
    }

    private Map<String, Object> buildBusinessOutput(int quantityRequested) {
        Map<String, Object> businessOutput = new LinkedHashMap<>();
        businessOutput.put("transportCompleted", true);
        businessOutput.put("unitsTransported", quantityRequested);
        businessOutput.put("damagedUnits", DAMAGED_UNITS);
        businessOutput.put("selectedResourceId", RESOURCE_ID);
        return businessOutput;
    }

    private EngineTaskCompletedEvent buildEngineTaskCompletedEvent(
            ActivatedJob job,
            Map<String, Object> variablesBefore,
            Map<String, Object> businessOutput,
            int quantityRequested,
            double batchMassKg
    ) {
        return EngineTaskCompletedEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EVENT_TYPE)
                .engineType(ENGINE_TYPE)
                .processDefinitionKey(job.getProcessDefinitionKey())
                .bpmnProcessId(job.getBpmnProcessId())
                .processInstanceKey(job.getProcessInstanceKey())
                .elementInstanceKey(job.getElementInstanceKey())
                .bpmnElementId(job.getElementId())
                .jobKey(job.getKey())
                .jobType(job.getType())
                .workerName(WORKER_NAME)
                .occurredAt(Instant.now())
                .variablesBefore(copyMap(variablesBefore))
                .businessOutput(businessOutput)
                .workerObservation(buildWorkerObservation(quantityRequested, batchMassKg))
                .build();
    }

    private Map<String, Object> buildWorkerObservation(int quantityRequested, double batchMassKg) {
        Map<String, Object> workerObservation = new LinkedHashMap<>();
        workerObservation.put("resourceId", RESOURCE_ID);
        workerObservation.put("resourceType", RESOURCE_TYPE);
        workerObservation.put("unitsProcessed", quantityRequested);
        workerObservation.put("payloadKg", batchMassKg);
        workerObservation.put("distanceMeters", DISTANCE_METERS);
        workerObservation.put("loadFactor", LOAD_FACTOR);
        workerObservation.put("actualEnergyKwh", null);
        workerObservation.put("measurementSource", null);
        workerObservation.put("observationSource", "simulated-worker");
        return workerObservation;
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
                    event.getProcessInstanceKey(),
                    event.getBpmnElementId(),
                    event.getJobKey(),
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

    private int readInt(Map<String, Object> variables, String name) {
        Object value = variables.get(name);
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String stringValue && !stringValue.isBlank()) {
            return Integer.parseInt(stringValue);
        }

        return 0;
    }

    private double readDouble(Map<String, Object> variables, String name) {
        Object value = variables.get(name);
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value instanceof String stringValue && !stringValue.isBlank()) {
            return Double.parseDouble(stringValue);
        }

        return 0.0;
    }

    private Map<String, Object> copyMap(Map<String, Object> source) {
        if (source == null) {
            return Map.of();
        }

        return new LinkedHashMap<>(source);
    }
}
