package cs.rug.gbpmruntime.engineintegration.infrastructure.camunda8.worker;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class TransportBatchWorker {

    private static final String ENGINE_TYPE = "CAMUNDA_8";
    private static final String JOB_TYPE = "transport-batch";
    private static final String WORKER_NAME = "transport-worker-1";
    private static final int DAMAGED_UNITS = 2;
    private static final int DISTANCE_METERS = 750;

    @JobWorker(type = JOB_TYPE, name = WORKER_NAME)
    public void handleTransportBatch(JobClient jobClient, ActivatedJob job) {
        Instant startedAt = Instant.now();
        Map<String, Object> variables = job.getVariablesAsMap();
        Map<String, Object> productionRequest = readMap(variables, "productionRequest");
        int quantityRequested = readInt(productionRequest, "quantityRequested");
        double batchMassKg = readDouble(productionRequest, "batchMassKg");
        DemoTransportResourceProfile resourceProfile = DemoTransportResourceProfile.transportUnit();

        Map<String, Object> businessOutput = buildBusinessOutput(quantityRequested, resourceProfile);
        jobClient
                .newCompleteCommand(job.getKey())
                .variables(businessOutput)
                .send()
                .join();

        Instant completedAt = Instant.now();
        Map<String, Object> workerObservation = buildWorkerObservation(
                startedAt,
                completedAt,
                quantityRequested,
                batchMassKg,
                resourceProfile
        );
        Camunda8TaskCompletionSnapshot snapshot = buildSnapshot(
                job,
                startedAt,
                completedAt,
                businessOutput,
                workerObservation
        );

        logTaskCompletionSnapshot(snapshot);
    }

    private Map<String, Object> buildBusinessOutput(int quantityRequested, DemoTransportResourceProfile resourceProfile) {
        Map<String, Object> businessOutput = new LinkedHashMap<>();
        businessOutput.put("transportCompleted", true);
        businessOutput.put("unitsTransported", quantityRequested);
        businessOutput.put("damagedUnits", DAMAGED_UNITS);
        businessOutput.put("selectedResourceId", resourceProfile.resourceId());
        return businessOutput;
    }

    private Map<String, Object> buildWorkerObservation(
            Instant startedAt,
            Instant completedAt,
            int quantityRequested,
            double batchMassKg,
            DemoTransportResourceProfile resourceProfile
    ) {
        double runtimeSeconds = Duration.between(startedAt, completedAt).toNanos() / 1_000_000_000.0;
        double activeSeconds = runtimeSeconds * 0.85;
        double idleSeconds = runtimeSeconds * 0.15;

        Map<String, Object> activityData = new LinkedHashMap<>();
        activityData.put("unitsProcessed", quantityRequested);
        activityData.put("payloadKg", batchMassKg);
        activityData.put("distanceMeters", DISTANCE_METERS);

        Map<String, Object> measurements = new LinkedHashMap<>();
        measurements.put("actualEnergyKwh", null);
        measurements.put("measurementSource", null);

        Map<String, Object> observationQuality = new LinkedHashMap<>();
        observationQuality.put("sourceType", "simulated-worker");
        observationQuality.put("simulationMethod", "deterministic-profile-based");
        observationQuality.put("confidence", "medium");

        Map<String, Object> resourceProfileRef = new LinkedHashMap<>();
        resourceProfileRef.put("resourceId", resourceProfile.resourceId());
        resourceProfileRef.put("profileVersion", "demo-2026.01");
        resourceProfileRef.put("energyModelType", resourceProfile.energyModelType());

        Map<String, Object> workerObservation = new LinkedHashMap<>();
        workerObservation.put("workerName", WORKER_NAME);
        workerObservation.put("workerType", JOB_TYPE);
        workerObservation.put("resourceId", resourceProfile.resourceId());
        workerObservation.put("resourceType", resourceProfile.resourceType());
        workerObservation.put("runtimeSeconds", runtimeSeconds);
        workerObservation.put("activeSeconds", activeSeconds);
        workerObservation.put("idleSeconds", idleSeconds);
        workerObservation.put("operatingMode", "standard");
        workerObservation.put("loadFactor", resourceProfile.defaultLoadFactor());
        workerObservation.put("activityData", activityData);
        workerObservation.put("measurements", measurements);
        workerObservation.put("observationQuality", observationQuality);
        workerObservation.put("resourceProfileRef", resourceProfileRef);
        return workerObservation;
    }

    private Camunda8TaskCompletionSnapshot buildSnapshot(
            ActivatedJob job,
            Instant startedAt,
            Instant completedAt,
            Map<String, Object> businessOutput,
            Map<String, Object> workerObservation
    ) {
        return new Camunda8TaskCompletionSnapshot(
                ENGINE_TYPE,
                job.getProcessDefinitionKey(),
                job.getBpmnProcessId(),
                job.getProcessInstanceKey(),
                job.getElementInstanceKey(),
                job.getElementId(),
                job.getKey(),
                job.getType(),
                WORKER_NAME,
                startedAt,
                completedAt,
                businessOutput,
                workerObservation
        );
    }

    private void logTaskCompletionSnapshot(Camunda8TaskCompletionSnapshot snapshot) {
        log.atInfo()
                .addKeyValue("eventType", "camunda8TaskCompletionSnapshot")
                .addKeyValue("engineType", snapshot.engineType())
                .addKeyValue("processDefinitionKey", snapshot.processDefinitionKey())
                .addKeyValue("bpmnProcessId", snapshot.bpmnProcessId())
                .addKeyValue("processInstanceKey", snapshot.processInstanceKey())
                .addKeyValue("elementInstanceKey", snapshot.elementInstanceKey())
                .addKeyValue("bpmnElementId", snapshot.bpmnElementId())
                .addKeyValue("jobKey", snapshot.jobKey())
                .addKeyValue("jobType", snapshot.jobType())
                .addKeyValue("workerName", snapshot.workerName())
                .addKeyValue("startedAt", snapshot.startedAt())
                .addKeyValue("completedAt", snapshot.completedAt())
                .addKeyValue("businessOutput", snapshot.businessOutput())
                .addKeyValue("workerObservation", snapshot.workerObservation())
                .log("Camunda 8 task completed: snapshot={}", snapshot);
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
}
