package cs.rug.camunda8integration.infrastructure.rest;

import cs.rug.camunda8integration.api.operations.completeusertask.CompleteUserTaskOperation;
import cs.rug.camunda8integration.api.operations.completeusertask.CompleteUserTaskRequest;
import cs.rug.camunda8integration.api.operations.findactiveusertasks.FindActiveUserTasksOperation;
import cs.rug.camunda8integration.api.operations.findactiveusertasks.FindActiveUserTasksRequest;
import cs.rug.camunda8integration.api.operations.findactiveusertasks.FindActiveUserTasksResponse;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/camunda8")
public class Camunda8UserTaskController {

    private final FindActiveUserTasksOperation findActiveUserTasksOperation;
    private final CompleteUserTaskOperation completeUserTaskOperation;

    @GetMapping("/process-instances/{processInstanceKey}/user-tasks")
    public ResponseEntity<FindActiveUserTasksResponse> findActiveUserTasks(
            @Pattern(regexp = "\\d+", message = "must be a numeric process instance key")
            @PathVariable String processInstanceKey
    ) {
        return ResponseEntity.ok(findActiveUserTasksOperation.process(FindActiveUserTasksRequest
                .builder()
                .processInstanceKey(processInstanceKey)
                .build()));
    }

    @PostMapping("/user-tasks/{userTaskKey}/completion")
    public ResponseEntity<Void> completeUserTask(
            @Pattern(regexp = "\\d+", message = "must be a numeric user task key")
            @PathVariable String userTaskKey,
            @RequestBody(required = false) Map<String, Object> variables
    ) {
        completeUserTaskOperation.process(CompleteUserTaskRequest
                .builder()
                .userTaskKey(userTaskKey)
                .variables(variables == null ? Map.of() : variables)
                .build());
        return ResponseEntity.noContent().build();
    }
}
