package cs.rug.monitoringresultsservice.api.operations.registerprocessmodel;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessModelKeiAnnotation {
    @NotBlank
    private String id;
    private String unit;
    private String targetValue;
    private String icon;
}
