package cs.rug.keievaluationservice.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationDescriptor {
    @NotBlank
    private String strategy;

    @NotBlank
    private String referenceSetId;
}
