package cs.rug.camunda8integration.infrastructure.client.mockoperational;

import cs.rug.camunda8integration.infrastructure.client.mockoperational.dto.CreateExecutionRunRequestDto;
import cs.rug.camunda8integration.infrastructure.client.mockoperational.dto.CreateExecutionRunResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "mockOperationalServiceClient",
        url = "${camunda8integration.clients.mock-operational-service.url}",
        path = "/api/execution-runs"
)
public interface MockOperationalServiceClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    CreateExecutionRunResponseDto createExecutionRun(
            @RequestBody CreateExecutionRunRequestDto request
    );
}
