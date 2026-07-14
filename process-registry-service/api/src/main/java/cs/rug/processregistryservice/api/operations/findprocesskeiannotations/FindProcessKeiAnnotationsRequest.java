package cs.rug.processregistryservice.api.operations.findprocesskeiannotations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindProcessKeiAnnotationsRequest {
    private Long processDefinitionKey;
}
