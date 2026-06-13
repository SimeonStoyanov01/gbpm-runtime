package cs.rug.gbpmruntime.processregistry.infrastructure.client.camunda8.dto.startprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessInEngineRequestDto {
    private String processDefinitionKey;
    private Map<String, Object> variables;
}
