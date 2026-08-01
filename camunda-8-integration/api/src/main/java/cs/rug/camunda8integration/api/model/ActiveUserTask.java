package cs.rug.camunda8integration.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActiveUserTask {
    private String userTaskKey;
    private String name;
    private String bpmnElementId;
    private String state;
    private String assignee;
    private String decisionVariable;
}
