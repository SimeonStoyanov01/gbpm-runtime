package cs.rug.processregistryservice.infrastructure.client.monitoring;

import cs.rug.processregistryservice.infrastructure.client.monitoring.dto.RegisterProcessModelRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "monitoringResultsClient",
        url = "${monitoring-results-service.url}",
        path = "/api/monitoring"
)
public interface MonitoringResultsFeignClient {

    @PostMapping(value = "/process-models", consumes = MediaType.APPLICATION_JSON_VALUE)
    void registerProcessModel(@RequestBody RegisterProcessModelRequest request);
}
