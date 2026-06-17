package cs.rug.monitoringresultsservice.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationDescriptor {
    private String type;
    private String method;
    private String version;
}
