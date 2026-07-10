package cs.rug.processregistryservice.infrastructure.client.camunda8;

import cs.rug.processregistryservice.infrastructure.client.camunda8.dto.deployprocess.DeployProcessToEngineRequestDto;
import cs.rug.processregistryservice.infrastructure.client.camunda8.dto.deployprocess.DeployProcessToEngineResponseDto;
import cs.rug.processregistryservice.infrastructure.client.camunda8.dto.startprocess.StartProcessInEngineRequestDto;
import cs.rug.processregistryservice.infrastructure.client.camunda8.dto.startprocess.StartProcessInEngineResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "camunda8IntegrationClient",
        url = "${camunda8-integration.url}",
        path = "/api/v1/camunda8/process-definitions"
)
public interface Camunda8IntegrationFeignClient {

    @PostMapping(value = "/deploy", consumes = MediaType.APPLICATION_JSON_VALUE)
    DeployProcessToEngineResponseDto deployProcessDefinition(
            @RequestBody DeployProcessToEngineRequestDto request
    );

    @PostMapping(value = "/instances", consumes = MediaType.APPLICATION_JSON_VALUE)
    StartProcessInEngineResponseDto startProcessInstance(
            @RequestBody StartProcessInEngineRequestDto request
    );
}
