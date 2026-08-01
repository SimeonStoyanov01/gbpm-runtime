package cs.rug.monitoringresultsservice.infrastructure.rest;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsOperation;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActiveViolationsController {

    private final FindActiveViolationsOperation findActiveViolationsOperation;

    @GetMapping("/api/monitoring/violations/active")
    public ResponseEntity<List<MonitoringRecord>> findActiveViolations(
            @RequestParam(required = false) Long processDefinitionKey,
            @RequestParam(required = false) String bpmnProcessId
    ) {
        List<MonitoringRecord> violations = findActiveViolationsOperation
                .process(FindActiveViolationsRequest
                        .builder()
                        .processDefinitionKey(processDefinitionKey)
                        .bpmnProcessId(bpmnProcessId)
                        .build());

        return ResponseEntity.ok(violations);
    }
}
