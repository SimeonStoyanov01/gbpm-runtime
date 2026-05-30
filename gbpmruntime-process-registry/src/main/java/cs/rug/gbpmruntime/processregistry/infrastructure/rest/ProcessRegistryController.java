package cs.rug.gbpmruntime.processregistry.infrastructure.rest;

import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.gbpmruntime.processregistry.api.operations.getprocessdefinition.GetProcessDefinitionOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.getprocessdefinition.GetProcessDefinitionRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.getprocessdefinition.GetProcessDefinitionResponse;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceOperation;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceRequest;
import cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance.StartProcessInstanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/process-definitions")
public class ProcessRegistryController {

    private final DeployProcessDefinitionOperation deployProcessDefinitionOperation;
    private final StartProcessInstanceOperation startProcessInstanceOperation;
    private final GetProcessDefinitionOperation getProcessDefinitionOperation;

    @PostMapping(value = "/deploy", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DeployProcessDefinitionResponse> deployProcessDefinition(
            @RequestPart("resource") MultipartFile resource,
            @RequestParam(value = "targetEngine", defaultValue = "CAMUNDA_8") String targetEngine
    ) throws IOException {
        DeployProcessDefinitionRequest request = DeployProcessDefinitionRequest
                .builder()
                .resourceName(resource.getOriginalFilename())
                .bpmnXml(resource.getBytes())
                .targetEngine(targetEngine)
                .build();
        return ResponseEntity.ok(deployProcessDefinitionOperation.process(request));
    }

    @PostMapping(value = "/{processDefinitionKey}/instances", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StartProcessInstanceResponse> startProcessInstance(
            @PathVariable String processDefinitionKey,
            @RequestBody Map<String, Object> variables
    ) {
        StartProcessInstanceRequest request = StartProcessInstanceRequest
                .builder()
                .processDefinitionKey(processDefinitionKey)
                .variables(variables)
                .build();
        return ResponseEntity.ok(startProcessInstanceOperation.process(request));
    }

    @GetMapping("/{processDefinitionKey}")
    public ResponseEntity<GetProcessDefinitionResponse> getProcessDefinition(
            @PathVariable String processDefinitionKey
    ) {
        GetProcessDefinitionRequest request = GetProcessDefinitionRequest
                .builder()
                .processDefinitionKey(processDefinitionKey)
                .build();
        return ResponseEntity.ok(getProcessDefinitionOperation.process(request));
    }
}
