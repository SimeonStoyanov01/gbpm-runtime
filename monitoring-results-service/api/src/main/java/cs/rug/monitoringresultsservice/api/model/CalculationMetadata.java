package cs.rug.monitoringresultsservice.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationMetadata {
    @NotBlank
    private String calculatorId;

    @NotBlank
    private String calculationMethod;

    @NotBlank
    private String referenceSetId;
}
