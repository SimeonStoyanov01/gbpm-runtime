package cs.rug.camunda8integration.application.processors.findactiveusertasks;

import cs.rug.camunda8integration.api.operations.findactiveusertasks.FindActiveUserTasksOperation;
import cs.rug.camunda8integration.api.operations.findactiveusertasks.FindActiveUserTasksRequest;
import cs.rug.camunda8integration.api.operations.findactiveusertasks.FindActiveUserTasksResponse;
import cs.rug.camunda8integration.application.out.Camunda8UserTaskClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindActiveUserTasksProcessor implements FindActiveUserTasksOperation {

    private final Camunda8UserTaskClient camunda8UserTaskClient;

    @Override
    public FindActiveUserTasksResponse process(FindActiveUserTasksRequest request) {
        return FindActiveUserTasksResponse
                .builder()
                .userTasks(camunda8UserTaskClient.findActiveUserTasks(request.getProcessInstanceKey()))
                .build();
    }
}
