package cs.rug.monitoringresultsservice.api.operations.registerprocessmodel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterProcessModelRequest {
    @NotNull
    @Positive
    private Long deploymentKey;

    @NotNull
    @Positive
    private Long processDefinitionKey;

    @NotBlank
    private String bpmnProcessId;

    @NotNull
    @Positive
    private Integer version;

    @NotBlank
    private String resourceName;

    @NotBlank
    private String bpmnXml;

    @Valid
    @NotNull
    private List<ProcessModelElement> elements;
}
