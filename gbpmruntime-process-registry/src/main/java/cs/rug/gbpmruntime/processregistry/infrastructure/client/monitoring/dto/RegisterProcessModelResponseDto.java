package cs.rug.gbpmruntime.processregistry.infrastructure.client.monitoring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterProcessModelResponseDto {
    private Long processDefinitionKey;
    private int elementCount;
    private int keiAnnotationCount;
}
