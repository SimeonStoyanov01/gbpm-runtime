package cs.rug.gbpmruntime.processregistry.api.operations.getprocessdefinition;

import cs.rug.gbpmruntime.common.api.base.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProcessDefinitionRequest implements ProcessorRequest {
    private String processDefinitionKey;
}
