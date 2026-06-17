package cs.rug.monitoringresultsservice.api.operations.registerprocessmodel;

import cs.rug.monitoringresultsservice.api.base.ProcessorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterProcessModelResponse implements ProcessorResponse {
    private Long processDefinitionKey;
    private int elementCount;
    private int keiAnnotationCount;
}
