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
public class KeiAnnotation {
    @NotBlank
    private String id;

    @NotBlank
    private String unit;

    private String targetValue;
    private String icon;
}
