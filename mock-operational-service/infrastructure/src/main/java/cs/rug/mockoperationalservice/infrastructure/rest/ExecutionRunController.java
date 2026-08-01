package cs.rug.mockoperationalservice.infrastructure.rest;

import cs.rug.mockoperationalservice.api.operations.createexecutionrun.CreateExecutionRunOperation;
import cs.rug.mockoperationalservice.api.operations.createexecutionrun.CreateExecutionRunRequest;
import cs.rug.mockoperationalservice.api.operations.createexecutionrun.CreateExecutionRunResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/execution-runs")
public class ExecutionRunController {

    private final CreateExecutionRunOperation createExecutionRunOperation;

    @PostMapping
    public ResponseEntity<CreateExecutionRunResponse> createExecutionRun(
            @Valid @RequestBody CreateExecutionRunRequest request
    ) {
        return ResponseEntity.ok(createExecutionRunOperation.process(request));
    }
}
