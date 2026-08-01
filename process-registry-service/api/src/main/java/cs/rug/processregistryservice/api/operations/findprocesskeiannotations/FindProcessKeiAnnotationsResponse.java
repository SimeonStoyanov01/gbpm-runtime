package cs.rug.processregistryservice.api.operations.findprocesskeiannotations;

import cs.rug.processregistryservice.api.model.ElementKeiAnnotations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindProcessKeiAnnotationsResponse {
    private List<ElementKeiAnnotations> elementKeiAnnotations;
}
