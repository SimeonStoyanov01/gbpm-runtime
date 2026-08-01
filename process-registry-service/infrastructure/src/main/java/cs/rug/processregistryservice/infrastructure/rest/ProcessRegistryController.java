package cs.rug.processregistryservice.infrastructure.rest;

import cs.rug.processregistryservice.api.exceptions.InvalidProcessDefinitionException;
import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionOperation;
import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionRequest;
import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.processregistryservice.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsOperation;
import cs.rug.processregistryservice.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsRequest;
import cs.rug.processregistryservice.api.operations.findactivitykeiannotations.FindActivityKeiAnnotationsResponse;
import cs.rug.processregistryservice.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsOperation;
import cs.rug.processregistryservice.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsRequest;
import cs.rug.processregistryservice.api.operations.findprocesskeiannotations.FindProcessKeiAnnotationsResponse;
import cs.rug.processregistryservice.api.operations.startprocessinstance.StartProcessInstanceOperation;
import cs.rug.processregistryservice.api.operations.startprocessinstance.StartProcessInstanceRequest;
import cs.rug.processregistryservice.api.operations.startprocessinstance.StartProcessInstanceResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.Map;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/process-definitions")
public class ProcessRegistryController {

    private final DeployProcessDefinitionOperation deployProcessDefinitionOperation;
    private final StartProcessInstanceOperation startProcessInstanceOperation;
    private final FindActivityKeiAnnotationsOperation findActivityKeiAnnotationsOperation;
    private final FindProcessKeiAnnotationsOperation findProcessKeiAnnotationsOperation;

    @PostMapping(value = "/deploy", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DeployProcessDefinitionResponse> deployProcessDefinition(
            @RequestPart("resource") MultipartFile resource,
            @NotBlank @RequestParam(value = "targetEngine", defaultValue = "CAMUNDA_8") String targetEngine
    ) throws IOException {
        if (resource.isEmpty()
                || resource.getOriginalFilename() == null
                || resource.getOriginalFilename().isBlank()) {
            throw new InvalidProcessDefinitionException();
        }

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
            @Pattern(regexp = "\\d+", message = "must be a numeric process definition key")
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

    @GetMapping("/{processDefinitionKey}/activities/{bpmnElementId}/keis")
    public ResponseEntity<FindActivityKeiAnnotationsResponse> findActivityKeiAnnotations(
            @Positive @PathVariable Long processDefinitionKey,
            @NotBlank @PathVariable String bpmnElementId
    ) {
        FindActivityKeiAnnotationsRequest request = FindActivityKeiAnnotationsRequest
                .builder()
                .processDefinitionKey(processDefinitionKey)
                .bpmnElementId(bpmnElementId)
                .build();
        return ResponseEntity.ok(findActivityKeiAnnotationsOperation.process(request));
    }

    @GetMapping("/{processDefinitionKey}/keis")
    public ResponseEntity<FindProcessKeiAnnotationsResponse> findProcessKeiAnnotations(
            @Positive @PathVariable Long processDefinitionKey
    ) {
        FindProcessKeiAnnotationsRequest request = FindProcessKeiAnnotationsRequest
                .builder()
                .processDefinitionKey(processDefinitionKey)
                .build();
        return ResponseEntity.ok(findProcessKeiAnnotationsOperation.process(request));
    }
}
