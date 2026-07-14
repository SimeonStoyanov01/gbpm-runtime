package cs.rug.monitoringresultsservice.infrastructure.rest;

import cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails.FindProcessInstanceDetailsOperation;
import cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails.FindProcessInstanceDetailsRequest;
import cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails.FindProcessInstanceDetailsResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequiredArgsConstructor
public class ProcessInstanceDetailsController {

    private final FindProcessInstanceDetailsOperation findProcessInstanceDetailsOperation;

    @GetMapping("/api/monitoring/process-instances/{processInstanceKey}")
    public ResponseEntity<FindProcessInstanceDetailsResponse> findProcessInstanceDetails(
            @Positive @PathVariable Long processInstanceKey
    ) {
        return ResponseEntity.ok(findProcessInstanceDetailsOperation.process(FindProcessInstanceDetailsRequest
                .builder()
                .processInstanceKey(processInstanceKey)
                .build()));
    }
}
