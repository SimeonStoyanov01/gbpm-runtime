package cs.rug.co2calculationservice.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationInputs {
    @Valid
    @NotEmpty
    private List<ResourceUsage> resourceUsages;
}
