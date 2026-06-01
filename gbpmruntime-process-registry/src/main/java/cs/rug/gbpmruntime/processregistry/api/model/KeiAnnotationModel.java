package cs.rug.gbpmruntime.processregistry.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeiAnnotationModel {
    private String id;
    private String unit;
    private String targetValue;
    private String icon;
}
