package cs.rug.monitoringresultsservice.infrastructure.rest;

import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelOperation;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelRequest;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProcessModelController {

    private final RegisterProcessModelOperation registerProcessModelOperation;

    @PostMapping("/api/monitoring/process-models")
    public ResponseEntity<RegisterProcessModelResponse> registerProcessModel(
            @RequestBody RegisterProcessModelRequest request
    ) {
        return ResponseEntity.ok(registerProcessModelOperation.process(request));
    }
}
