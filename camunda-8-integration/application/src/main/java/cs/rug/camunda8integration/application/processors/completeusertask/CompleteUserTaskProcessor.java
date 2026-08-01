package cs.rug.camunda8integration.application.processors.completeusertask;

import cs.rug.camunda8integration.api.operations.completeusertask.CompleteUserTaskOperation;
import cs.rug.camunda8integration.api.operations.completeusertask.CompleteUserTaskRequest;
import cs.rug.camunda8integration.application.out.Camunda8UserTaskClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompleteUserTaskProcessor implements CompleteUserTaskOperation {

    private final Camunda8UserTaskClient camunda8UserTaskClient;

    @Override
    public void process(CompleteUserTaskRequest request) {
        camunda8UserTaskClient.completeUserTask(request.getUserTaskKey(), request.getVariables());
    }
}
