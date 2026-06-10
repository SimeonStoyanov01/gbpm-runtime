package cs.rug.gbpmruntime.processregistry.infrastructure.clients.dto.startprocess;

import cs.rug.gbpmruntime.common.api.base.ProcessorRequest;
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
