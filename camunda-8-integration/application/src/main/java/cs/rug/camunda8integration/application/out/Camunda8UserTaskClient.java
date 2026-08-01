package cs.rug.camunda8integration.application.out;

import cs.rug.camunda8integration.api.model.ActiveUserTask;

import java.util.List;
import java.util.Map;

public interface Camunda8UserTaskClient {

    List<ActiveUserTask> findActiveUserTasks(String processInstanceKey);

    void completeUserTask(String userTaskKey, Map<String, Object> variables);
}
