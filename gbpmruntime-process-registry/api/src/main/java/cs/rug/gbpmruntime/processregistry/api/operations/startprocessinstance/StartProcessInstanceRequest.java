package cs.rug.gbpmruntime.processregistry.api.operations.startprocessinstance;

import cs.rug.gbpmruntime.processregistry.api.base.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessInstanceRequest implements ProcessorRequest {
    private String processDefinitionKey;
    private Map<String, Object> variables;
}
