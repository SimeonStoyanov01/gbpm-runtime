package cs.rug.processregistryservice.api.operations.findprocesskeiannotations;

import cs.rug.processregistryservice.api.base.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindProcessKeiAnnotationsRequest implements ProcessorRequest {
    private Long processDefinitionKey;
}
