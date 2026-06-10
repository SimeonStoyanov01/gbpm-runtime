package cs.rug.camunda8integration.api.operations.startprocess;

import cs.rug.camunda8integration.api.base.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessInEngineRequest implements ProcessorRequest {
    private String processDefinitionKey;
    private Map<String, Object> variables;
}
