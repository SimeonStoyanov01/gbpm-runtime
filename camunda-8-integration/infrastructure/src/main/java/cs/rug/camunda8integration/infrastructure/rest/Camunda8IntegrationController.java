package cs.rug.camunda8integration.infrastructure.rest;

import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineOperation;
import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineRequest;
import cs.rug.camunda8integration.api.operations.deployprocess.DeployProcessToEngineResponse;
import cs.rug.camunda8integration.api.operations.startprocess.StartProcessInEngineOperation;
import cs.rug.camunda8integration.api.operations.startprocess.StartProcessInEngineRequest;
import cs.rug.camunda8integration.api.operations.startprocess.StartProcessInEngineResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/camunda8/process-definitions")
public class Camunda8IntegrationController {

    private final DeployProcessToEngineOperation deployProcessToEngineOperation;
    private final StartProcessInEngineOperation startProcessInEngineOperation;

    @PostMapping(value = "/deploy", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeployProcessToEngineResponse> deployProcessDefinition(
            @Valid @RequestBody DeployProcessToEngineRequest request
    ) {
        return ResponseEntity.ok(deployProcessToEngineOperation.process(request));
    }

    @PostMapping(value = "/instances", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StartProcessInEngineResponse> startProcessInstance(
            @Valid @RequestBody StartProcessInEngineRequest request
    ) {
        return ResponseEntity.ok(startProcessInEngineOperation.process(request));
    }
}
