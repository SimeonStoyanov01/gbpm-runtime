package cs.rug.co2calculationservice.application.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class EmissionFactor {
    private String fuelType;
    private String unit;
    private BigDecimal factor;
}
