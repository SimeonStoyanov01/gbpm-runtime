package cs.rug.processregistryservice.infrastructure.client.camunda8.dto.startprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessInEngineRequest {
    private String processDefinitionKey;
    private Map<String, Object> variables;
}
