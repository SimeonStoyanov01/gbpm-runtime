package cs.rug.co2calculationservice.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kei {
    private String id;
    private String unit;
    private String targetValue;
}
