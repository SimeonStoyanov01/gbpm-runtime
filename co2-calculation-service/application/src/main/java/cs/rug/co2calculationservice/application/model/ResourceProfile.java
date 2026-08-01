package cs.rug.co2calculationservice.application.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ResourceProfile {
    private String name;
    private String type;
    private BigDecimal fuelPerUse;
    private String fuelType;
    private String fuelUnit;
    private String timeUnit;
}
