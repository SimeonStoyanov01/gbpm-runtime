package cs.rug.camunda8integration.infrastructure.client.camunda8;

import cs.rug.camunda8integration.api.exceptions.EngineUserTaskException;
import cs.rug.camunda8integration.api.model.ActiveUserTask;
import cs.rug.camunda8integration.application.out.Camunda8UserTaskClient;
import io.camunda.client.CamundaClient;
import io.camunda.client.api.search.enums.UserTaskState;
import io.camunda.client.api.search.response.UserTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Camunda8UserTaskClientAdapter implements Camunda8UserTaskClient {

    private final CamundaClient camundaClient;
    private final UserTaskDecisionVariableResolver decisionVariableResolver;

    @Override
    public List<ActiveUserTask> findActiveUserTasks(String processInstanceKey) {
        try {
            return camundaClient
                    .newUserTaskSearchRequest()
                    .filter(filter -> filter
                            .processInstanceKey(Long.parseLong(processInstanceKey))
                            .state(UserTaskState.CREATED))
                    .send()
                    .join()
                    .items()
                    .stream()
                    .map(this::toActiveUserTask)
                    .toList();
        } catch (RuntimeException exception) {
            throw new EngineUserTaskException("Failed to find active Camunda 8 user tasks.", exception);
        }
    }

    @Override
    public void completeUserTask(String userTaskKey, Map<String, Object> variables) {
        try {
            camundaClient
                    .newCompleteUserTaskCommand(Long.parseLong(userTaskKey))
                    .variables(variables)
                    .send()
                    .join();
        } catch (RuntimeException exception) {
            throw new EngineUserTaskException("Failed to complete Camunda 8 user task.", exception);
        }
    }

    private ActiveUserTask toActiveUserTask(UserTask userTask) {
        return ActiveUserTask
                .builder()
                .userTaskKey(String.valueOf(userTask.getUserTaskKey()))
                .name(userTask.getName())
                .bpmnElementId(userTask.getElementId())
                .state(userTask.getState().name())
                .assignee(userTask.getAssignee())
                .decisionVariable(decisionVariableResolver.resolve(userTask))
                .build();
    }
}
