package cs.rug.processregistryservice.api.operations.startprocessinstance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessInstanceRequest {
    private String processDefinitionKey;
    private Map<String, Object> variables;
}
