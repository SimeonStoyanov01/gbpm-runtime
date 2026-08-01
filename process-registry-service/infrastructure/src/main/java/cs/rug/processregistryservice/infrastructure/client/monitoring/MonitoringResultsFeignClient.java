package cs.rug.processregistryservice.infrastructure.client.monitoring;

import cs.rug.processregistryservice.infrastructure.client.monitoring.dto.ProcessModelElement;
import cs.rug.processregistryservice.infrastructure.client.monitoring.dto.RegisterProcessModelRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "monitoringResultsClient",
        url = "${monitoring-results-service.url}",
        path = "/api/monitoring"
)
public interface MonitoringResultsFeignClient {

    @PostMapping(value = "/process-models", consumes = MediaType.APPLICATION_JSON_VALUE)
    void registerProcessModel(@RequestBody RegisterProcessModelRequest request);

    @GetMapping("/process-models/{processDefinitionKey}/elements")
    List<ProcessModelElement> findProcessModel(@PathVariable Long processDefinitionKey);
}
