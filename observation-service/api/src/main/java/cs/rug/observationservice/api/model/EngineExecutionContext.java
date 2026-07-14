package cs.rug.observationservice.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineExecutionContext {
    @NotBlank
    private String engineType;

    @NotNull
    private Long processDefinitionKey;

    @NotBlank
    private String bpmnProcessId;

    @NotNull
    private Long processInstanceKey;

    @NotNull
    private Long elementInstanceKey;

    @NotBlank
    private String bpmnElementId;
}
