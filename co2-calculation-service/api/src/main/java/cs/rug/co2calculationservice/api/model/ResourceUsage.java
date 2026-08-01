package cs.rug.co2calculationservice.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceUsage {
    @NotBlank
    private String resourceName;

    @NotNull
    @Positive
    private Double timeUsed;

    @NotBlank
    private String unit;
}
