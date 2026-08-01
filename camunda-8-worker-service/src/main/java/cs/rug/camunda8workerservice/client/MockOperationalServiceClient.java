package cs.rug.camunda8workerservice.client;

import cs.rug.camunda8workerservice.client.dto.CreateExecutionRunRequest;
import cs.rug.camunda8workerservice.client.dto.CreateExecutionRunResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "mockOperationalServiceClient",
        url = "${camunda8worker.clients.mock-operational-service.url}",
        path = "/api/execution-runs"
)
public interface MockOperationalServiceClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    CreateExecutionRunResponse createExecutionRun(
            @RequestBody CreateExecutionRunRequest request
    );
}
