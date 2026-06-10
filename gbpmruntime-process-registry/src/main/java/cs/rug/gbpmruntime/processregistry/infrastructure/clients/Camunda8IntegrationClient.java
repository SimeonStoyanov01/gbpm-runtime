package cs.rug.gbpmruntime.processregistry.infrastructure.clients;

import cs.rug.gbpmruntime.processregistry.infrastructure.clients.dto.deployprocess.DeployProcessToEngineRequest;
import cs.rug.gbpmruntime.processregistry.infrastructure.clients.dto.deployprocess.DeployProcessToEngineResponse;
import cs.rug.gbpmruntime.processregistry.infrastructure.clients.dto.startprocess.StartProcessInEngineRequest;
import cs.rug.gbpmruntime.processregistry.infrastructure.clients.dto.startprocess.StartProcessInEngineResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "camunda8IntegrationClient",
        url = "${camunda8-integration.url}",
        path = "/api/v1/camunda8/process-definitions"
)
public interface Camunda8IntegrationClient {

    @PostMapping(value = "/deploy", consumes = MediaType.APPLICATION_JSON_VALUE)
    DeployProcessToEngineResponse deployProcessDefinition(
            @RequestBody DeployProcessToEngineRequest request
    );

    @PostMapping(value = "/instances", consumes = MediaType.APPLICATION_JSON_VALUE)
    StartProcessInEngineResponse startProcessInstance(
            @RequestBody StartProcessInEngineRequest request
    );
}