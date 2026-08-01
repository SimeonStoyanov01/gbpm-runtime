package cs.rug.monitoringresultsservice.api.operations.registerprocessmodel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessModelElement {
    @NotBlank
    private String bpmnElementId;

    private String name;

    @NotBlank
    private String type;

    @Valid
    @NotNull
    private List<ProcessModelKeiAnnotation> keiAnnotations;
}
