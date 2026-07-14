package cs.rug.camunda8integration.api.operations.findactiveusertasks;

import cs.rug.camunda8integration.api.model.ActiveUserTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindActiveUserTasksResponse {
    private List<ActiveUserTask> userTasks;
}
