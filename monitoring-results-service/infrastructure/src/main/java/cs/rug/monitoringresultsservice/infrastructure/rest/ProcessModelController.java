package cs.rug.monitoringresultsservice.infrastructure.rest;

import cs.rug.monitoringresultsservice.api.operations.findprocessmodel.FindProcessModelOperation;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.ProcessModelElement;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelOperation;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProcessModelController {

    private final RegisterProcessModelOperation registerProcessModelOperation;
    private final FindProcessModelOperation findProcessModelOperation;

    @PostMapping("/api/monitoring/process-models")
    public ResponseEntity<Void> registerProcessModel(
            @Valid @RequestBody RegisterProcessModelRequest request
    ) {
        registerProcessModelOperation.process(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/monitoring/process-models/{processDefinitionKey}/elements")
    public ResponseEntity<List<ProcessModelElement>> findProcessModel(
            @PathVariable Long processDefinitionKey
    ) {
        return ResponseEntity.ok(findProcessModelOperation.process(processDefinitionKey));
    }
}
