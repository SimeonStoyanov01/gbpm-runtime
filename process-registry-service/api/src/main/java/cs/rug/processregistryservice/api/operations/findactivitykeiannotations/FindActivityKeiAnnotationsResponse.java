package cs.rug.processregistryservice.api.operations.findactivitykeiannotations;

import cs.rug.processregistryservice.api.base.ProcessorResponse;
import cs.rug.processregistryservice.api.model.KeiAnnotationModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindActivityKeiAnnotationsResponse implements ProcessorResponse {
    private List<KeiAnnotationModel> bpmn4esKeiAnnotations;
}
