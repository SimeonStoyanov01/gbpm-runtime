package cs.rug.gbpmruntime.processregistry.api.operations.getprocessdefinition;

import cs.rug.gbpmruntime.common.api.base.ProcessorResponse;
import cs.rug.gbpmruntime.processregistry.api.model.ProcessDefinitionRegistrationModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProcessDefinitionResponse implements ProcessorResponse {
    private ProcessDefinitionRegistrationModel registration;
}
