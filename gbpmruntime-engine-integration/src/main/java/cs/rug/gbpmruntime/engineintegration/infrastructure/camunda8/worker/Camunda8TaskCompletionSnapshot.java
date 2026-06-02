package cs.rug.gbpmruntime.engineintegration.infrastructure.camunda8.worker;

import java.time.Instant;
import java.util.Map;

public record Camunda8TaskCompletionSnapshot(
        String engineType,
        long processDefinitionKey,
        String bpmnProcessId,
        long processInstanceKey,
        long elementInstanceKey,
        String bpmnElementId,
        long jobKey,
        String jobType,
        String workerName,
        Instant startedAt,
        Instant completedAt,
        Map<String, Object> businessOutput,
        Map<String, Object> workerObservation
) {
}
