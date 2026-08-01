package cs.rug.processregistryservice.api.operations.findactivitykeiannotations;

import cs.rug.processregistryservice.api.model.KeiAnnotation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindActivityKeiAnnotationsResponse {
    private List<KeiAnnotation> keiAnnotations;
}
